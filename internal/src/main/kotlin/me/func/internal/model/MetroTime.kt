package me.func.internal.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "metro_times")
data class MetroTime(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    val id: Int = 0,

    @Column(name = "id_st1")
    val idSt1: String,

    @Column(name = "id_st2")
    val idSt2: String,

    @Column(name = "time")
    val time: Double
)