package me.func.internal.dto

import com.fasterxml.jackson.annotation.JsonFormat
import java.sql.Timestamp

data class CreateApplicationRequest(
    val idPas: String,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    val datetime: Timestamp,
    val inspSexM: Int = 0,
    val inspSexF: Int = 0,
    val idSt1: Int = 1,
    val idSt2: Int = 1
)
