package me.func.internal.model

import java.time.LocalDateTime
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "passenger_no_show")
data class PassengerNoShow(
    @Id
    @Column(name = "id_bid")
    val idBid: String,

    @Column(name = "date_time")
    val dateTime: LocalDateTime
)