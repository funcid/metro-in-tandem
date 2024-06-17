package me.func.internal.dto

import java.sql.Time
import java.sql.Timestamp

interface ApplicationPassengerInfo {
    val id: String
    val idPas: String
    val time3: Time
    val time4: Time
    val timeOver: Time
    val status: String
    val catPas: String
    val datetime: Timestamp
    val fullName: String
    val number: String?
    val tpz: Timestamp
    val idSt1: String
    val idSt2: String
}