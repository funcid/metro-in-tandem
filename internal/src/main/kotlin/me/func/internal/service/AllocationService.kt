package me.func.internal.service

import me.func.internal.dto.ExtendedApplication
import me.func.internal.model.Application
import me.func.internal.model.Employee
import me.func.internal.repository.ApplicationRepository
import me.func.internal.repository.EmployeeRepository
import org.springframework.stereotype.Service
import java.sql.Timestamp
import java.time.Instant
import java.time.LocalTime

@Service
class AllocationService(
    private val applicationRepository: ApplicationRepository,
    private val employeeRepository: EmployeeRepository,
    private val pathfinderService: PathfinderService
) {
    private val allocation = mutableMapOf<Employee, MutableList<ExtendedApplication>>()

    fun allocateApplications(from: Long, to: Long): Map<Employee, List<ExtendedApplication>> {
        val employees = employeeRepository.findAll()
        val applications = applicationRepository.findAllByDatetimeBetween(
            Timestamp.from(Instant.ofEpochMilli(from)),
            Timestamp.from(Instant.ofEpochMilli(to + 1000 * 60 * 60 * 24)),
        ).filter { !it.status.isCancelled() }

        // Sort employees by priority: CI first, then CSI
        val ciEmployees = employees.filter { it.rank == "ЦИ" }
        val csiEmployees = employees.filter { it.rank == "ЦСИ" }

        // Initialize allocation with empty lists
        (ciEmployees + csiEmployees).forEach { allocation[it] = mutableListOf() }

        // Main allocation algorithm
        applications.forEach { app ->
            val suitableEmployees = findSuitableEmployees(ciEmployees, app)
                .ifEmpty { findSuitableEmployees(csiEmployees, app) }

            if (suitableEmployees.isNotEmpty()) {
                val employeesToTime = suitableEmployees.associateWith { employee ->
                    estimateTravelTime(allocation[employee]?.maxByOrNull { it.application.time4 }?.application, app)
                }
                employeesToTime.minByOrNull { it.value }?.let { (key, value) ->
                    allocation[key]?.add(
                        ExtendedApplication(
                            application = app,
                            travelTime = value
                        )
                    )
                }
            }
        }

        // Return allocation with extended application data
        return allocation
    }

    fun calculateLunchTime(employee: Employee, extendedApplications: List<ExtendedApplication>): LocalTime {
        var applications = extendedApplications
            .sortedByDescending { it.application.datetime }
            .toMutableList()

        if (applications.size % 2 != 0) {
            applications = applications.dropLast(1).toMutableList()
        }
        if (applications.size <= 1) {
            return LocalTime.now()
        }
        val pair = applications
            .zipWithNext()
            .filter {
                it.first.application.time3.toLocalTime().toSecondOfDay() -
                it.second.application.time4.toLocalTime().toSecondOfDay() -
                it.first.travelTime * 60 >= 3560
            }.maxByOrNull {
                it.first.application.time3.toLocalTime().toSecondOfDay() -
                it.second.application.time4.toLocalTime().toSecondOfDay() -
                it.first.travelTime * 60
            }
        pair?.let {
            return pair.second.application.time4.toLocalTime()
        }
        return LocalTime.now()
    }

    private fun findSuitableEmployees(employees: List<Employee>, application: Application): List<Employee> {
        return employees.filter { employee ->
            isEmployeeAvailable(employee, application) &&
                    isEmployeeMatchingSexRequirement(employee, application) &&
                    isLunchTimeValid(employee, application)
        }
    }

    private fun isEmployeeAvailable(employee: Employee, application: Application): Boolean {
        val applicationStart = application.time3.toLocalTime().minusMinutes(15)
        val applicationEnd = application.time4.toLocalTime().plusMinutes(10)
        val employeeApplications = allocation[employee] ?: emptyList()

        val (workStart, workEnd) = parseWorkTime(employee.timeWork)

        return employeeApplications.none { existingApplication ->
            val existingStart = existingApplication.application.time3.toLocalTime()
            val existingEnd = existingApplication.application.time4.toLocalTime()
            !(applicationEnd.isBefore(existingStart) || applicationStart.isAfter(existingEnd))
        } && isWithinWorkHours(applicationStart, applicationEnd, workStart, workEnd)
    }

    private fun parseWorkTime(timeWork: String): Pair<LocalTime, LocalTime> {
        val times = timeWork.split("-")
        val startWork = LocalTime.parse(times[0])
        val endWork = LocalTime.parse(times[1])
        return Pair(startWork, endWork)
    }

    private fun isWithinWorkHours(start: LocalTime, end: LocalTime, workStart: LocalTime, workEnd: LocalTime): Boolean {
        return if (workEnd.isAfter(workStart)) {
            start.isAfter(workStart) && end.isBefore(workEnd)
        } else {
            start.isAfter(workStart) || end.isBefore(workEnd)
        }
    }

    private fun isEmployeeMatchingSexRequirement(employee: Employee, application: Application): Boolean {
        return when (employee.sex) {
            "Мужской" -> application.inspSexM > 0
            "Женский" -> application.inspSexF > 0
            else -> false
        }
    }

    private fun isLunchTimeValid(employee: Employee, application: Application): Boolean {
        val (startWork, endWork) = parseWorkTime(employee.timeWork)
        val applicationEnd = application.time4.toLocalTime()

        val potentialLunchTime = calculatePotentialLunchTime(startWork, applicationEnd)
        return isWithinWorkHours(startWork, potentialLunchTime.first, endWork.minusHours(1), potentialLunchTime.second)
    }

    private fun calculatePotentialLunchTime(
        workStart: LocalTime,
        applicationEnd: LocalTime
    ): Pair<LocalTime, LocalTime> {
        val lunchStart = maxOf(workStart.plusHours(3).plusMinutes(30), applicationEnd.plusMinutes(10))
        val lunchEnd = lunchStart.plusHours(1)
        return Pair(lunchStart, lunchEnd)
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
}
