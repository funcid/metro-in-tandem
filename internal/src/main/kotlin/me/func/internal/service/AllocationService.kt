package me.func.internal.service

import me.func.internal.model.Application
import me.func.internal.model.Employee
import me.func.internal.repository.ApplicationRepository
import me.func.internal.repository.EmployeeRepository
import org.springframework.stereotype.Service
import java.time.LocalTime

@Service
class AllocationService(
    private val applicationRepository: ApplicationRepository,
    private val employeeRepository: EmployeeRepository,
    private val pathfinderService: PathfinderService
) {
    private val allocation = mutableMapOf<Employee, MutableList<Application>>()

    fun allocateApplications(): Map<Employee, List<Application>> {
        val employees = employeeRepository.findAll()
        val applications = applicationRepository.findAll().filter { !it.status.isCancelled() }

        // Сортируем сотрудников по приоритету ЦИ и ЦСИ
        val ciEmployees = employees.filter { it.rank == "ЦИ" }
        val csiEmployees = employees.filter { it.rank == "ЦСИ" }

        // Инициализация распределения пустыми списками
        (ciEmployees + csiEmployees).forEach { allocation[it] = mutableListOf() }

        // Основной алгоритм распределения
        applications.forEach { app ->
            val suitableEmployees = findSuitableEmployees(ciEmployees, app)
                .ifEmpty { findSuitableEmployees(csiEmployees, app) }

            if (suitableEmployees.isNotEmpty()) {
                val bestEmployee = suitableEmployees.minByOrNull {
                    estimateTravelTime(allocation[it]?.lastOrNull(), app)
                }
                bestEmployee?.let {
                    allocation[it]?.add(app)
                }
            }
        }

        return allocation
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

        return employeeApplications.none { existingApplication ->
            val existingStart = existingApplication.time3.toLocalTime()
            val existingEnd = existingApplication.time4.toLocalTime()
            !(applicationEnd.isBefore(existingStart) || applicationStart.isAfter(existingEnd))
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
        val startWork = LocalTime.parse(employee.timeWork.split("-")[0])
        val endWork = LocalTime.parse(employee.timeWork.split("-")[1])
        val applicationEnd = application.time4.toLocalTime()
        val lunchStart = startWork.plusHours(3).plusMinutes(30)
        val lunchEnd = endWork.minusHours(1)

        if (allocation[employee]?.any {
                it.time4.toLocalTime().isAfter(lunchStart) && it.time3.toLocalTime().isBefore(lunchEnd)
            } == true) {
            return true
        }

        val potentialLunchStart = applicationEnd.plusMinutes(10)
        return potentialLunchStart.isAfter(lunchStart) && potentialLunchStart.isBefore(lunchEnd)
    }

    private fun estimateTravelTime(lastApplication: Application?, newApplication: Application): Double {
        return if (lastApplication == null) {
            0.0
        } else {
            val path = pathfinderService.findPath(lastApplication.idSt1.toInt(), newApplication.idSt2.toInt())
            path.second
        }
    }
}