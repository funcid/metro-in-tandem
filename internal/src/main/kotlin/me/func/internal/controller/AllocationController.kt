package me.func.internal.controller

import me.func.internal.dto.AllocationResponse
import me.func.internal.service.AllocationService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1")
class AllocationController(
    private val allocationService: AllocationService
) {
    @GetMapping("/allocations")
    fun getAllocations(
        @RequestParam("from") from: Long,
        @RequestParam("to") to: Long,
    ): List<AllocationResponse> {
        val allocations = allocationService.allocateApplications(from, to)
        return allocations.map { (employee, applications) ->
            AllocationResponse(employee, applications)
        }
    }
}