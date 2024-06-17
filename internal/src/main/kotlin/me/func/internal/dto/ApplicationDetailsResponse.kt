package me.func.internal.dto

import com.fasterxml.jackson.annotation.JsonFormat
import me.func.internal.model.MetroStation
import java.sql.Time
import java.sql.Timestamp

data class ApplicationDetailsResponse(
    val idPas: String,
    val fio: String,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy HH:mm:ss")
    val datetime: Timestamp,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
    val time3: Time,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
    val time4: Time,
    val catPas: String,
    val status: String,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy HH:mm:ss")
    val tpz: Timestamp,
    val inspSexM: Int,
    val inspSexF: Int,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
    val timeOver: Time,
    val stationFrom: MetroStation?,
    val stationTo: MetroStation?,
    val duration: String,
    val transplants: Int,
)