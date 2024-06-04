package me.func.internal.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDate

@Entity
@Table(name = "employees")
data class Employee(
    @Id
    @Column(name = "id")
    val id: String,

    @Column(name = "date")
    val date: LocalDate,

    @Column(name = "time_work")
    val timeWork: String,

    @Column(name = "fio")
    val fio: String,

    @Column(name = "uchastok")
    val uchastok: String,

    @Column(name = "smena")
    val smena: String,

    @Column(name = "rank")
    val rank: String,

    @Column(name = "sex")
    val sex: String
)