package me.func.internal.service

import jakarta.transaction.Transactional
import me.func.internal.model.Allocation
import me.func.internal.model.Allocation.Companion.createLunch
import me.func.internal.model.Allocation.Companion.fromApplication
import me.func.internal.model.AllocationType
import me.func.internal.model.Application
import me.func.internal.model.Employee
import me.func.internal.repository.AllocationRepository
import me.func.internal.repository.ApplicationRepository
import me.func.internal.repository.EmployeeRepository
import org.springframework.stereotype.Service
import java.sql.Timestamp
import java.time.Instant
import java.time.LocalDateTime
import java.time.LocalTime

@Service
@Transactional
class AllocationService(
    private val applicationRepository: ApplicationRepository,
    private val employeeRepository: EmployeeRepository,
    private val pathfinderService: PathfinderService,
    private val allocationRepository: AllocationRepository,
) {
    private var allocation = mutableMapOf<Employee, MutableList<Allocation>>()

    fun allocateApplications(from: Long, to: Long, reallocate: Boolean): Map<Employee, List<Allocation>> {
        allocation.clear()

        val employees = employeeRepository.findAll().toList().distinctBy { it.id }
        val applications = findValidApplications(from, to).distinctBy { it.id }
        if (reallocate) {

            allocationRepository.deleteAll()

            allocateToSuitableEmployees(employees, applications)
            ensureLunchBreakForAllEmployees()
            logFailedAllocations()

            allocationRepository.saveAllAndFlush(allocation.values.flatten())
        } else {
            val db = allocationRepository.findAll()

            allocation = db.groupBy { it.employeeId }
                .mapValues { (_, value) -> value.toMutableList() }
                .mapKeys { employees.find { e -> it.key == e.id }!! }
                .toMutableMap()
        }



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

        applications.sortedBy { it.time4 }.forEach { app ->
            val suitableEmployees = findSuitableEmployees(ciEmployees, csiEmployees, app)

            if (suitableEmployees.isNotEmpty()) {
                val (employee, travelTime) = findBestEmployeeAndTravelTime(suitableEmployees, app, applications)
                allocation[employee]?.addAll(app.fromApplication(employee, travelTime))
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

    private fun findBestEmployeeAndTravelTime(
        employees: List<Employee>, application: Application,
        applications: List<Application>
    ): Pair<Employee, Double> {
        return employees.map { employee ->
            employee to estimateTravelTime(
                applications.find { app ->
                    app.id == allocation[employee]
                        ?.filter { it.type == AllocationType.APPLICATION }
                        ?.maxByOrNull { it.to }
                        ?.applicationId
                },
                application
            )
        }.minByOrNull { it.second } ?: throw IllegalStateException("No suitable employee found")
    }

    private fun ensureLunchBreakForAllEmployees() {
        allocation.keys.forEach { employee ->
            val extendedApplications = allocation[employee] ?: return@forEach
            val lunchTime = calculateLunchTime(employee, extendedApplications)
            println("lunch time for ${employee.fio} $lunchTime (converts to ${Timestamp.valueOf(lunchTime)}) from $extendedApplications")
            if (lunchTime != null) {
                allocation[employee]?.add(employee.createLunch(lunchTime))
            }
        }
    }

    private fun calculateLunchTime(employee: Employee, extendedApplications: List<Allocation>): LocalDateTime? {
        extendedApplications.sortedBy { it.from }

        val (workStart, _) = parseWorkTime(employee)

        var lunchTime = workStart.plusMinutes(60 * 3 + 30)
        println("Work start for ${employee.fio} is $workStart $lunchTime")

        while (true) {
            val overlap = extendedApplications.lastOrNull {
                it.from.toLocalDateTime().isBefore(lunchTime.plusHours(1)) &&
                    it.to.toLocalDateTime().isAfter(lunchTime)
            }
            if (overlap == null) break
            lunchTime = overlap.to.toLocalDateTime()
        }

        return lunchTime
    }

    private fun isEmployeeAvailable(employee: Employee, application: Application): Boolean {
        val applicationStart = application.time3.toLocalTime()
            .toLocalDateTime(employee)
            .minusMinutes(15)
        val applicationEnd = application.time4.toLocalTime()
            .toLocalDateTime(employee)
            .plusMinutes(10)

        val employeeApplications = allocation[employee] ?: emptyList()

        val (workStart, workEnd) = parseWorkTime(employee)
        val workHoursCrossMidnight = workEnd.isBefore(workStart)

        if (employee.id == 709L && application.id == 489193L) {
            println("a")
        }
        return employeeApplications.none { existingApplication ->
            val existingStart = existingApplication.from.toLocalDateTime()
            val existingEnd = existingApplication.to.toLocalDateTime()
            !(applicationEnd.isBefore(existingStart) || applicationStart.isAfter(existingEnd))
        } && isWithinWorkHours(applicationStart, applicationEnd, workStart, workEnd, workHoursCrossMidnight)
    }

    private fun parseWorkTime(employee: Employee): Pair<LocalDateTime, LocalDateTime> {
        val times = employee.timeWork.split("-")

        return LocalTime.parse(times[0]).toLocalDateTime(employee) to
            LocalTime.parse(times[1]).toLocalDateTime(employee)
    }

    private fun isWithinWorkHours(
        start: LocalDateTime,
        end: LocalDateTime,
        workStart: LocalDateTime,
        workEnd: LocalDateTime,
        crossesMidnight: Boolean
    ): Boolean {
        return if (!crossesMidnight) {
            start.isAfter(workStart) && end.isBefore(workEnd)
        } else {
            start.isAfter(workStart) || end.isBefore(workEnd)
        }
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

    private fun LocalTime.toLocalDateTime(employee: Employee): LocalDateTime {
        val startOfDay = employee.date.atStartOfDay()
        return startOfDay.plusSeconds(this.toSecondOfDay().toLong())
    }
}
