package me.func.internal.dto

import me.func.internal.model.MetroStation

data class ApplicationResponse(
    val id: String,
    val idPas: String,
    val time3: String,
    val time4: String,
    val timeOver: String,
    val status: String,
    val catPas: String,
    val datetime: String,
    val fullName: String,
    val mobileNumber: String,
    val tpz: String,
    val stationFrom: MetroStation?,
    val stationTo: MetroStation?,
)