package me.func.internal.controller

import me.func.internal.dto.AllocationResponse
import me.func.internal.service.AllocationService
import me.func.internal.service.ApplicationService
import me.func.internal.service.UserService
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1")
@PreAuthorize("hasAnyAuthority('Сотрудник')")
class DayController(
    private val allocationService: AllocationService,
    private val applicationService: ApplicationService,
    private val userService: UserService
) {
    @GetMapping("/day")
    fun getAllocations(
        @RequestParam("from") from: Long,
        @RequestParam("to") to: Long,
    ): AllocationResponse {
        val auth = SecurityContextHolder.getContext().authentication
        val username = auth.name

        val user = userService.findByUsername(username)
            ?: throw UsernameNotFoundException("User not found by username: $username")
        val employee = user.employee

        val allocations = allocationService.findByEmployeeId(employee.id!!)

        return AllocationResponse(
            employee = employee,
            allocations = allocations,
            applications = applicationService.getApplicationsByIds(allocations.mapNotNull { it.applicationId })
        )
    }
}
