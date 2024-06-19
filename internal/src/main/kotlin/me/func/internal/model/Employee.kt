package me.func.internal.model

import jakarta.persistence.*
import me.func.internal.converter.StringListConverter
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
    val sex: String,

    @Column(name = "work_phone")
    val workPhone: String,

    @Column(name = "personal_phone")
    val personalPhone: String,

    @Column(name = "employee_id")
    val employeeID: String,

    @Column(name = "light_duty")
    val lightDuty: Boolean,

    @Convert(converter = StringListConverter::class)
    @Column(name = "vacations")
    val vacations: List<String> = emptyList(),

    @Column(name = "sick_leaves")
    @Convert(converter = StringListConverter::class)
    val sickLeaves: List<String> = emptyList(),

    @Column(name = "days_off")
    @Convert(converter = StringListConverter::class)
    val daysOff: List<String> = emptyList()
)
