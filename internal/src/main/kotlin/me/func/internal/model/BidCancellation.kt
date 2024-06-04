package me.func.internal.model

import jakarta.persistence.Entity
import jakarta.persistence.Column
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "bid_cancellations")
data class BidCancellation(
    @Id
    @Column(name = "id_bid")
    val idBid: String,
    @Column(name = "date_time")
    val dateTime: LocalDateTime
)