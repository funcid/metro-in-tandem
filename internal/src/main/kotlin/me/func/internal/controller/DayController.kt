package me.func.internal.controller

import me.func.internal.dto.AllocationResponse
import me.func.internal.model.AllocationType
import me.func.internal.model.Application
import me.func.internal.service.AllocationService
import me.func.internal.service.ApplicationService
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1")
@PreAuthorize("hasAnyAuthority('Сотрудник')")
class DayController(
    private val allocationService: AllocationService,
    private val applicationService: ApplicationService
) {
    @GetMapping("/day")
    fun getAllocations(
        @RequestParam("from") from: Long,
        @RequestParam("to") to: Long,
    ): List<AllocationResponse> {
        val (employee, allocations) = allocationService.allocateApplications(from, to, reallocate = false)
            .entries
            .first()

        return allocations.map {
            AllocationResponse(
                employee = employee,
                allocations = allocations,
                applications = applicationService.getApplicationsByIds(allocations.mapNotNull { it.applicationId })
            )
        }
    }

}
