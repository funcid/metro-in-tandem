package me.func.internal.dto

import me.func.internal.model.Application
import me.func.internal.model.Employee

data class AllocationResponse(
    val employee: Employee,
    val applications: List<Application>
)