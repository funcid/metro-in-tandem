package me.func.internal.dto

import me.func.internal.model.Employee
import java.time.LocalTime

data class AllocationResponse(
    val employee: Employee,
    val lunchTime: LocalTime?,
    val applications: List<ExtendedApplication>
)