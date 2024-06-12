package me.func.internal.dto

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.sql.Time
import java.sql.Timestamp

@JsonIgnoreProperties(ignoreUnknown = true)
data class ApplicationUpdateRequest(
    val status: String,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy HH:mm:ss")
    val datetime: Timestamp,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
    val time3: Time,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
    val time4: Time,
    val inspSexM: Int,
    val inspSexF: Int,
    val idSt1: String,
    val idSt2: String,
)