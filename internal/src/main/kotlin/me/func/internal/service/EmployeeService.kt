package me.func.internal.service

import me.func.internal.model.Employee
import me.func.internal.repository.EmployeeRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
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
}