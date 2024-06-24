package me.func.internal.dto

import me.func.internal.model.MetroStation

data class ApplicationResponse(
    val id: String,
    val idPas: String,
    val timeOver: String,
    val status: String,
    val datetime: String,
    val fullName: String,
    val mobileNumber: String,
    val stationFrom: MetroStation?,
    val stationTo: MetroStation?,
    val employeeFio: String? = null,
    val employeeId: Long? = null,
)