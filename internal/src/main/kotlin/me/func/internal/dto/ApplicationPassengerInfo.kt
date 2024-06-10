package me.func.internal.dto

import java.sql.Time
import java.sql.Timestamp

interface ApplicationPassengerInfo {
    val id: String
    val time3: Time
    val time4: Time
    val timeOver: Time
    val status: String?
    val catPas: String
    val datetime: Timestamp
    val fullName: String
    val number: String?
}