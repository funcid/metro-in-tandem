package me.func.internal.service

import me.func.internal.dto.ExtendedApplication
import me.func.internal.model.*
import me.func.internal.repository.ApplicationRepository
import me.func.internal.repository.EmployeeRepository
import org.springframework.stereotype.Service
import java.sql.Time
import java.sql.Timestamp
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

@Service
class AllocationService(
    private val applicationRepository: ApplicationRepository,
    private val employeeRepository: EmployeeRepository,
    private val pathfinderService: PathfinderService
) {
    private val allocation = mutableMapOf<Employee, MutableList<ExtendedApplication>>()

    fun allocateApplications(from: Long, to: Long): Map<Employee, List<ExtendedApplication>> {
        val employees = employeeRepository.findAll().toList()
        val applications = findValidApplications(from, to)

        allocateToSuitableEmployees(employees, applications)

        ensureLunchBreakForAllEmployees()

        logFailedAllocations()

        return allocation
    }

    private fun findValidApplications(from: Long, to: Long): List<Application> {
        val startTimestamp = Timestamp.from(Instant.ofEpochMilli(from))
        val endTimestamp = Timestamp.from(Instant.ofEpochMilli(to + 1000 * 60 * 60 * 24))

        return applicationRepository.findAllByDatetimeBetween(startTimestamp, endTimestamp)
            .filter { !it.status.isCancelled() }
    }

    private fun allocateToSuitableEmployees(employees: List<Employee>, applications: List<Application>) {
        val ciEmployees = employees.filter { it.rank == "ЦИ" }
        val csiEmployees = employees.filter { it.rank == "ЦСИ" }

        (ciEmployees + csiEmployees).forEach { employee ->
            allocation[employee] = mutableListOf()
        }

        applications.forEach { app ->
            val suitableEmployees = findSuitableEmployees(ciEmployees, csiEmployees, app)

            if (suitableEmployees.isNotEmpty()) {
                val (employee, travelTime) = findBestEmployeeAndTravelTime(suitableEmployees, app)
                allocation[employee]?.add(ExtendedApplication(app, travelTime))
            } else {
                println("Не удалось найти подходящего сотрудника для заявки ${app.id}")
            }
        }
    }

    private fun findSuitableEmployees(
        ciEmployees: List<Employee>,
        csiEmployees: List<Employee>,
        application: Application
    ): List<Employee> {
        val suitableCiEmployees = findSuitableEmployees(ciEmployees, application)
        return suitableCiEmployees.ifEmpty { findSuitableEmployees(csiEmployees, application) }
    }

    private fun findSuitableEmployees(employees: List<Employee>, application: Application): List<Employee> {
        return employees.filter { employee ->
            isEmployeeAvailable(employee, application)
        }
    }

    private fun findBestEmployeeAndTravelTime(employees: List<Employee>, application: Application): Pair<Employee, Double> {
        return employees.map { employee ->
            employee to estimateTravelTime(allocation[employee]?.maxByOrNull { it.application.time4 }?.application, application)
        }.minByOrNull { it.second } ?: throw IllegalStateException("No suitable employee found")
    }

    private fun ensureLunchBreakForAllEmployees() {
        allocation.keys.forEach { employee ->
            val extendedApplications = allocation[employee] ?: return@forEach
            val lunchTime = calculateLunchTime(employee, extendedApplications)
            if (lunchTime != null) {
                allocation[employee]?.add(ExtendedApplication(createLunchApplication(employee, lunchTime), 0.0))
            } else {
                forceLunchBreak(employee)
            }
        }
    }

    private fun createLunchApplication(employee: Employee, lunchStart: LocalTime): Application {
        return Application(
            id = -1 * employee.id!!,
            time3 = Time.valueOf(lunchStart.minusMinutes(10)),
            time4 = Time.valueOf(lunchStart.plusHours(1)),
            datetime = Timestamp.valueOf(lunchStart.atDate(LocalDate.now())),
            status = ApplicationStatus.ACCEPTED,
            inspSexM = 0,
            inspSexF = 0,
            idSt1 = "1",
            idSt2 = "1",
            catPas = PassengerCategory.NO,
            idPas = "0",
            timeOver = Time.valueOf(LocalTime.now()),
            tpz = Timestamp.valueOf(LocalDateTime.now())
        )
    }

    private fun calculateLunchTime(employee: Employee, extendedApplications: List<ExtendedApplication>): LocalTime? {
        val sortedApplications = extendedApplications.sortedBy { it.application.time3 }

        val (workStart, workEnd) = parseWorkTime(employee.timeWork)
        val workHoursCrossMidnight = workEnd.isBefore(workStart)

        for (i in 0 until sortedApplications.size - 1) {
            val currentApplication = sortedApplications[i]
            val nextApplication = sortedApplications[i + 1]

            val availableTime = nextApplication.application.time3.toLocalTime()
                .toSecondOfDay() - currentApplication.application.time4.toLocalTime().toSecondOfDay()

            if (availableTime >= 3600) {
                val potentialLunchStart = currentApplication.application.time4.toLocalTime().plusMinutes(10)
                val potentialLunchEnd = potentialLunchStart.plusHours(1)

                if (isValidLunchTime(potentialLunchStart, potentialLunchEnd, workStart, workEnd, sortedApplications, workHoursCrossMidnight)) {
                    return potentialLunchStart
                }
            }
        }

        return null
    }

    private fun forceLunchBreak(employee: Employee) {
        val (workStart, workEnd) = parseWorkTime(employee.timeWork)
        val workHoursCrossMidnight = workEnd.isBefore(workStart)
        val forcedLunchStart = if (!workHoursCrossMidnight) {
            workStart.plusHours(3).plusMinutes(30)
        } else {
            workStart.plusHours(3).plusMinutes(30).takeIf { it.isBefore(LocalTime.MIDNIGHT) } ?: LocalTime.MIDNIGHT
        }
        allocation[employee]?.add(ExtendedApplication(createLunchApplication(employee, forcedLunchStart), 0.0))
    }

    private fun isEmployeeAvailable(employee: Employee, application: Application): Boolean {
        val applicationStart = application.time3.toLocalTime().minusMinutes(15)
        val applicationEnd = application.time4.toLocalTime().plusMinutes(10)
        val employeeApplications = allocation[employee] ?: emptyList()

        val (workStart, workEnd) = parseWorkTime(employee.timeWork)
        val workHoursCrossMidnight = workEnd.isBefore(workStart)

        return employeeApplications.none { existingApplication ->
            val existingStart = existingApplication.application.time3.toLocalTime()
            val existingEnd = existingApplication.application.time4.toLocalTime()
            !(applicationEnd.isBefore(existingStart) || applicationStart.isAfter(existingEnd))
        } && isWithinWorkHours(applicationStart, applicationEnd, workStart, workEnd, workHoursCrossMidnight)
    }

    private fun parseWorkTime(timeWork: String): Pair<LocalTime, LocalTime> {
        val times = timeWork.split("-")
        val startWork = LocalTime.parse(times[0])
        val endWork = LocalTime.parse(times[1])
        return Pair(startWork, endWork)
    }

    private fun isWithinWorkHours(start: LocalTime, end: LocalTime, workStart: LocalTime, workEnd: LocalTime, crossesMidnight: Boolean): Boolean {
        return if (!crossesMidnight) {
            start.isAfter(workStart) && end.isBefore(workEnd)
        } else {
            start.isAfter(workStart) || end.isBefore(workEnd)
        }
    }

    private fun isValidLunchTime(
        lunchStart: LocalTime,
        lunchEnd: LocalTime,
        workStart: LocalTime,
        workEnd: LocalTime,
        applications: List<ExtendedApplication>,
        crossesMidnight: Boolean
    ): Boolean {
        val isWithinHours = if (!crossesMidnight) {
            lunchStart.isAfter(workStart.plusHours(3).plusMinutes(30)) &&
                    lunchEnd.isBefore(workEnd.minusHours(1))
        } else {
            (lunchStart.isAfter(workStart.plusHours(3).plusMinutes(30)) && lunchEnd.isBefore(LocalTime.MAX)) ||
                    (lunchStart.isAfter(LocalTime.MIN) && lunchEnd.isBefore(workEnd.minusHours(1)))
        }

        val doesNotOverlap = applications.none { application ->
            val appStart = application.application.time3.toLocalTime()
            val appEnd = application.application.time4.toLocalTime()
            !(lunchEnd.isBefore(appStart) || lunchStart.isAfter(appEnd))
        }

        return isWithinHours && doesNotOverlap
    }

    private fun estimateTravelTime(lastApplication: Application?, newApplication: Application): Double {
        return when {
            lastApplication == null -> 0.0
            else -> pathfinderService.findPath(
                startStationId = lastApplication.idSt2.toInt(),
                endStationId = newApplication.idSt1.toInt()
            ).second
        }
    }

    private fun logFailedAllocations() {
        val failedAllocations = allocation.filterValues { it.size == 0 }.keys
        if (failedAllocations.isNotEmpty()) {
            println("Не удалось распределить заявки следующим сотрудникам:")
            failedAllocations.forEach { employee ->
                println("${employee.fio} (${employee.rank})")
            }
        }
    }
}
