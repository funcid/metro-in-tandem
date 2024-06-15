package me.func.internal.controller

import me.func.internal.model.Application
import me.func.internal.model.Employee
import me.func.internal.service.AllocationService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1")
class AllocationController(
    private val allocationService: AllocationService
) {
    @GetMapping("/allocations")
    fun getAllocations(): List<AllocationDTO> {
        val allocations = allocationService.allocateApplications()
        return allocations.map { (employee, applications) ->
            AllocationDTO(employee, applications)
        }
    }
}

data class AllocationDTO(
    val employee: Employee,
    val applications: List<Application>
)