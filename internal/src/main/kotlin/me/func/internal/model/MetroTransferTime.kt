package me.func.internal.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "metro_transfer_times")
data class MetroTransferTime(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Чтобы указать, что `id` генерируется автоматически
    @Column(name = "id")
    val id: Int = 0,

    @Column(name = "time")
    val time: Int,

    @Column(name = "id1")
    val id1: String,

    @Column(name = "id2")
    val id2: String
)