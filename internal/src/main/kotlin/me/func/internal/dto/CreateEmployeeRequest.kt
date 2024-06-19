package me.func.internal.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class CreateEmployeeRequest(
    val fullName: String,
    val uchastok: String,
    val initials: String,
    val gender: String,
    val shift: String,
    val position: String,
    val workTime: String,
    val workPhone: String,
    val personalPhone: String,
    val employeeID: String,
    val lightDuty: Boolean = false,
    val vacations: List<String> = emptyList(),
    val sickLeaves: List<String> = emptyList(),
    val daysOff: List<String> = emptyList()
)
