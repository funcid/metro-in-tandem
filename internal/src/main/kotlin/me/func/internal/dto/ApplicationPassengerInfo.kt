package me.func.internal.dto

import java.sql.Time
import java.sql.Timestamp

interface ApplicationPassengerInfo {
    val id: String
    val idPas: String
    val timeOver: Time
    val status: String
    val datetime: Timestamp
    val fullName: String
    val number: String?
    val stationFromId: Int
    val stationFromName: String
    val stationFromLine: String
    val stationFromLineId: Int
    val stationToId: Int
    val stationToName: String
    val stationToLine: String
    val stationToLineId: Int
    val employeeFio: String?
    val employeeId: Long?
}
