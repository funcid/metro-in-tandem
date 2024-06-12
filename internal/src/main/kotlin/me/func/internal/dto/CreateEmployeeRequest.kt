package me.func.internal.dto

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
    val lightDuty: Boolean
)