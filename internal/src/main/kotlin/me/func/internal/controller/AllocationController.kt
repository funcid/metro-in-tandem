package me.func.internal.controller

import me.func.internal.dto.AllocationResponse
import me.func.internal.service.AllocationService
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.LocalTime

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
    ): List<AllocationResponse> {
        return allocationService.allocateApplications(from, to).map { (employee, applications) ->
            AllocationResponse(
                employee = employee,
                applications = applications,
                lunchTime = applications.find { it.application.idPas == "0" }?.application?.time3?.toLocalTime(),
            )
        }
    }
}