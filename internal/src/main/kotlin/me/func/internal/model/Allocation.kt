package me.func.internal.model

import com.fasterxml.jackson.annotation.JsonFormat
import jakarta.persistence.*
import java.sql.Timestamp
import java.time.Instant
import java.time.LocalDateTime

@Entity
@Table(name = "allocation")
data class Allocation(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long = 0,

    @Column(name = "employee_id", nullable = false)
    val employeeId: Long,

    @Column(name = "application_id", nullable = true)
    val applicationId: Long? = null,

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    val type: AllocationType,

    @Column(name = "fr", nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy HH:mm:ss", timezone = "Europe/Moscow")
    val from: Timestamp,

    @Column(name = "destination", nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy HH:mm:ss", timezone = "Europe/Moscow")
    val to: Timestamp,

    @Column(name = "allocation_time", nullable = false)
    val allocationTime: Timestamp = Timestamp(System.currentTimeMillis())
) {

    companion object {
        fun Employee.createLunch(from: LocalDateTime): Allocation {
            return Allocation(
                employeeId = this.id!!,
                type = AllocationType.LUNCH_BREAK,
                from = Timestamp.valueOf(from),
                to = Timestamp.valueOf(from.plusHours(1)),
                allocationTime = Timestamp.from(Instant.now()),
            )
        }

        fun Application.fromApplication(employee: Employee, travelTimeMinutes: Double): List<Allocation> {
            val startOfDay = datetime.toLocalDateTime().toLocalDate().atStartOfDay()
            val from = startOfDay
                .plusSeconds(time3.toLocalTime().toSecondOfDay().toLong())
            val to = startOfDay
                .plusSeconds(time4.toLocalTime().toSecondOfDay().toLong())

            return listOf(
                    Allocation(
                    employeeId = employee.id!!,
                    applicationId = this.id,
                    allocationTime = Timestamp.from(Instant.now()),
                    type = AllocationType.APPLICATION,
                    from = Timestamp.valueOf(from),
                    to = Timestamp.valueOf(to),
                ), Allocation(
                    employeeId = employee.id,
                    applicationId = this.id,
                    allocationTime = Timestamp.from(Instant.now()),
                    type = AllocationType.TRAVEL,
                    from = Timestamp.valueOf(from.minusSeconds(Math.round(travelTimeMinutes * 60))),
                    to = Timestamp.valueOf(from),
                )
            )
        }
    }
}
