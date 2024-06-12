package me.func.internal.model

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.persistence.*
import me.func.internal.converter.PassengerCategoryConverter
import me.func.internal.converter.ApplicationStatusConverter
import java.sql.Time
import java.sql.Timestamp

@Entity
@Table(name = "applications")
data class Application(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0L,

    @Column(name = "id_pas")
    @JsonProperty("id_pas")
    val idPas: String,

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy HH:mm:ss")
    val datetime: Timestamp,

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
    val time3: Time,

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
    val time4: Time,

    @Column(name = "cat_pas")
    @JsonProperty("cat_pas")
    @Convert(converter = PassengerCategoryConverter::class)
    val catPas: PassengerCategory,

    @Convert(converter = ApplicationStatusConverter::class)
    val status: ApplicationStatus,

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