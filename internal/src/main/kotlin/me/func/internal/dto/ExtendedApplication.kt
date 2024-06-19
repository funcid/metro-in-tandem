package me.func.internal.dto

import me.func.internal.model.Application

data class ExtendedApplication(
    val application: Application,
    val travelTime: Double,
)
