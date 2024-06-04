package me.func.internal.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.sql.Time
import java.sql.Timestamp

@Entity
@Table(name = "applications")
data class Application(
    @Id
    val id: String,
    @Column(name  =  "id_pas")
    val idPas: String,
    val datetime: Timestamp,
    val time3: Time,
    val time4: Time,
    @Column(name  =  "cat_pas")
    val catPas: String,
    val status: String,
    val tpz: Timestamp,
    @Column(name  =  "insp_sex_m")
    val inspSexM: Int,
    @Column(name  =  "insp_sex_f")
    val inspSexF: Int,
    @Column(name  =  "time_over")
    val timeOver: Time,
    @Column(name  =  "id_st1")
    val idSt1: String,
    @Column(name  =  "id_st2")
    val idSt2: String
)