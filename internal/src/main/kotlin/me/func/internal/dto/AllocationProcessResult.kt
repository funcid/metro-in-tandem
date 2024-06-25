package me.func.internal.dto

import me.func.internal.model.Allocation
import me.func.internal.model.Application
import me.func.internal.model.Employee

data class AllocationProcessResult(
    val allocations: Map<Employee, List<Allocation>>,
    val failedApplications: List<Application>
)