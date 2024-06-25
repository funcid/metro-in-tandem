package me.func.internal.controller

import me.func.internal.dto.AllocationOperationResponse
import me.func.internal.dto.AllocationResponse
import me.func.internal.model.AllocationType
import me.func.internal.service.AllocationService
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1")
@PreAuthorize("hasAnyAuthority('Администратор', 'Специалист')")
class AllocationController(
    private val allocationService: AllocationService
) {
    @GetMapping("/allocations")
    fun getAllocations(
        @RequestParam("from") from: Long,
        @RequestParam("to") to: Long,
    ): AllocationOperationResponse {
        val result = allocationService.allocateApplications(from, to, reallocate = false)
        return AllocationOperationResponse(
            allocations = result.allocations.map { (employee, allocations) ->
                AllocationResponse(
                    employee = employee,
                    allocations = allocations,
                )
            },
            failedToAllocate = result.failedApplications
        )
    }

    @PostMapping("/reallocate")
    fun reallocateApplications(
        @RequestParam("from") from: Long,
        @RequestParam("to") to: Long,
    ): AllocationOperationResponse {
        val result = allocationService.allocateApplications(from, to, reallocate = true)
        return AllocationOperationResponse(
            allocations = result.allocations.map { (employee, allocations) ->
                AllocationResponse(
                    employee = employee,
                    allocations = allocations,
                )
            },
            failedToAllocate = result.failedApplications
        )
    }
}
