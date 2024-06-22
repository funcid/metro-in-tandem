package me.func.internal.model

import com.fasterxml.jackson.annotation.JsonFormat
import jakarta.persistence.*
import java.sql.Timestamp
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.*

@Entity
@Table(name = "allocation")
data class Allocation(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    val employee: Employee,

    @ManyToOne
    @JoinColumn(name = "application_id", nullable = true)
    val application: Application? = null,

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    val type: AllocationType,

    @Column(name = "fr", nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy HH:mm:ss")
    val from: Timestamp,

    @Column(name = "destination", nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy HH:mm:ss")
    val to: Timestamp,

    @Column(name = "allocation_time", nullable = false)
    val allocationTime: Timestamp = Timestamp(System.currentTimeMillis())
) {

    companion object {
        fun Employee.createLunch(from: LocalDateTime): Allocation {
            return Allocation(
                employee = this,
                type = AllocationType.LUNCH_BREAK,
                from = Timestamp.from(from.toInstant(ZoneOffset.UTC)),
                to = Timestamp.from(from.plusHours(1).toInstant(ZoneOffset.UTC)),
                allocationTime = Timestamp.from(Instant.now()),
            )
        }

        fun Application.fromApplication(employee: Employee, travelTimeMinutes: Double): List<Allocation> {
            val startOfDay = datetime
                .toLocalDateTime()
                .toLocalDate()
                .atStartOfDay()
                .toInstant(ZoneOffset.UTC)
            val from = startOfDay.toEpochMilli() + time3
                .toLocalTime()
                .toSecondOfDay() * 1_000
            val to = startOfDay.toEpochMilli() + time4
                .toLocalTime()
                .toSecondOfDay() * 1_000

            return listOf(
                    Allocation(
                    employee = employee,
                    application = this,
                    allocationTime = Timestamp.from(Instant.now()),
                    type = AllocationType.APPLICATION,
                    from = Timestamp.from(Date(from).toInstant()),
                    to = Timestamp.from(Date(to).toInstant()),
                ), Allocation(
                    employee = employee,
                    application = this,
                    allocationTime = Timestamp.from(Instant.now()),
                    type = AllocationType.TRAVEL,
                    from = Timestamp.from(Date(from).toInstant().minusSeconds(Math.round(travelTimeMinutes * 60))),
                    to = Timestamp.from(Date(from).toInstant()),
                )
            )
        }
    }
}
