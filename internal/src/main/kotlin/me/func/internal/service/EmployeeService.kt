package me.func.internal.service

import jakarta.transaction.Transactional
import me.func.internal.dto.CreateEmployeeRequest
import me.func.internal.model.Employee
import me.func.internal.repository.EmployeeRepository
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.util.*
import kotlin.NoSuchElementException

@Service
@Transactional
class EmployeeService(private val employeeRepository: EmployeeRepository) {

    fun getEmployeeById(id: String): Optional<Employee> {
        return employeeRepository.findById(id)
    }

    fun updateEmployee(id: String, updatedEmployee: Employee): Employee {
        if (!employeeRepository.existsById(id)) {
            throw NoSuchElementException("Employee with id $id not found")
        }
        return employeeRepository.save(updatedEmployee)
    }

    fun getEmployeesByRegion(region: String): List<Employee> {
        return employeeRepository.findByUchastok(region)
    }

    fun createEmployee(request: CreateEmployeeRequest): Employee {
        val newEmployee = Employee(
            date = LocalDate.now(),
            timeWork = request.workTime,
            fio = request.initials,
            uchastok = request.uchastok,
            smena = request.shift,
            rank = request.position,
            sex = request.gender,
            workPhone = request.workPhone,
            personalPhone = request.personalPhone,
            employeeID = request.employeeID,
            lightDuty = request.lightDuty,
            vacations = request.vacations,
            sickLeaves  = request.sickLeaves,
            daysOff  = request.daysOff,
        )

        return employeeRepository.save(newEmployee)
    }

    fun deleteEmployee(id: String)  {
        employeeRepository.deleteById(id)
    }
}
