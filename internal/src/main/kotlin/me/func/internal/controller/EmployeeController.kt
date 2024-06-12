package me.func.internal.controller

import me.func.internal.dto.CreateEmployeeRequest
import me.func.internal.model.Employee
import me.func.internal.service.EmployeeService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/employees")
class EmployeeController(private val employeeService: EmployeeService) {

    @GetMapping("/{id}")
    fun getEmployeeById(@PathVariable id: String): ResponseEntity<Employee> {
        val employee = employeeService.getEmployeeById(id)
        return if (employee.isPresent) {
            ResponseEntity.ok(employee.get())
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @PutMapping("/{id}")
    fun updateEmployee(
        @PathVariable id: String,
        @RequestBody updatedEmployee: Employee
    ): ResponseEntity<Employee> {
        return try {
            val employee = employeeService.updateEmployee(id, updatedEmployee)
            ResponseEntity.ok(employee)
        } catch (e: NoSuchElementException) {
            ResponseEntity.notFound().build()
        }
    }

    @GetMapping
    fun getEmployeesByRegion(@RequestParam("region") region: String): ResponseEntity<List<Employee>> {
        val employees = employeeService.getEmployeesByRegion(region)
        return if (employees.isNotEmpty()) {
            ResponseEntity.ok(employees)
        } else {
            ResponseEntity.noContent().build()
        }
    }

    @PostMapping
    fun createEmployee(@RequestBody request: CreateEmployeeRequest): ResponseEntity<Employee> {
        val createdEmployee = employeeService.createEmployee(request)
        return ResponseEntity.status(HttpStatus.CREATED).body(createdEmployee)
    }
}