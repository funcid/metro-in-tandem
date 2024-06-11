package me.func.internal.dto

import me.func.internal.model.ContactNumber

data class PassengerResponse(
    val id: Long,
    val fullName: String,
    val contactNumbers: Set<ContactNumber> = emptySet(),
    val gender: String,
    val category: String,
    val additionalInfo: String? = null,
    val hasPacemaker: Boolean = false
)