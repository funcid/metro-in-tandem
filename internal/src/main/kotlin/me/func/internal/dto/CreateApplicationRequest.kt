package me.func.internal.dto

import com.fasterxml.jackson.annotation.JsonFormat
import me.func.internal.model.ApplicationStatus
import me.func.internal.model.PassengerCategory
import java.sql.Timestamp

data class CreateApplicationRequest(
    val idPas: String,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy HH:mm:ss")
    val dateTime: Timestamp,
    val catPas: PassengerCategory,
    val status: ApplicationStatus,
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy HH:mm:ss")
//    val tpz: Timestamp, как с этим быть
    val inspSexM: Int,
    val inspSexF: Int,
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
//    val timeOver: Time, как с этим быть
    val idSt1: String,
    val idSt2: String
)
