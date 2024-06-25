package me.func.internal.dto

import me.func.internal.model.Application

data class AllocationOperationResponse(
    val allocations: List<AllocationResponse>,
    val failedToAllocate: List<Application>
)