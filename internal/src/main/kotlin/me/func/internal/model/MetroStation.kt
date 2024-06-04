package me.func.internal.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "metro_stations")
data class MetroStation(
    @Id
    @Column(name = "id")
    val id: String,

    @Column(name = "name_station")
    val nameStation: String,

    @Column(name = "name_line")
    val nameLine: String,

    @Column(name = "id_line")
    val idLine: String
)