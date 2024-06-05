package me.func.internal.model

import RequestStatus
import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.persistence.*
import me.func.internal.converter.PassengerCategoryConverter
import me.func.internal.converter.RequestStatusConverter
import me.func.internal.dto.PassengerCategory
import java.sql.Time
import java.sql.Timestamp

@Entity
@Table(name = "applications")
data class Application(
    @Id
    var id: String,

    @Column(name = "id_pas")
    @JsonProperty("id_pas")
    val idPas: String,

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy HH:mm:ss")
    val datetime: Timestamp,

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
    val time3: Time,

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
    val time4: Time,

    @Enumerated(EnumType.STRING)
    @Column(name = "cat_pas")
    @JsonProperty("cat_pas")
    @Convert(converter = PassengerCategoryConverter::class)
    val catPas: PassengerCategory,

    @Enumerated(EnumType.STRING)
    @Convert(converter = RequestStatusConverter::class)
    val status: RequestStatus,

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy HH:mm:ss")
    val tpz: Timestamp,

    @Column(name = "insp_sex_m")
    @JsonProperty("INSP_SEX_M")
    val inspSexM: Int,

    @Column(name = "insp_sex_f")
    @JsonProperty("INSP_SEX_F")
    val inspSexF: Int,

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
    @Column(name = "time_over")
    @JsonProperty("TIME_OVER")
    val timeOver: Time,

    @Column(name = "id_st1")
    val idSt1: String,

    @Column(name = "id_st2")
    val idSt2: String
)