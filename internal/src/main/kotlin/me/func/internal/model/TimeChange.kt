package me.func.internal.model

import java.time.LocalTime
import java.time.LocalDateTime
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "time_changes")
data class TimeChange(
    @Id
    @Column(name = "id_bid")
    val idBid: String,

    @Column(name = "time_edit")
    val timeEdit: LocalDateTime? = null,

    @Column(name = "time_s")
    val timeS: LocalTime? = null,

    @Column(name = "time_f")
    val timeF: LocalTime? = null
)