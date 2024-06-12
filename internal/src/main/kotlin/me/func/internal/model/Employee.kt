package me.func.internal.model

import jakarta.persistence.*
import java.time.LocalDate

@Entity
@Table(name = "employees")
data class Employee(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

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