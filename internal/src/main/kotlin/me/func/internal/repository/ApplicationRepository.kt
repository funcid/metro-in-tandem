package me.func.internal.repository

import me.func.internal.model.ApplicationStatus
import me.func.internal.dto.ApplicationPassengerInfo
import me.func.internal.model.Application
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.data.rest.core.annotation.RepositoryRestResource
import java.sql.Timestamp
import java.time.LocalDateTime

@RepositoryRestResource(collectionResourceRel = "applications", path = "applications")
interface ApplicationRepository : CrudRepository<Application, Long> {

    fun findByStatus(status: ApplicationStatus): List<Application>

    @Query("""
        SELECT a.id, a.time3, a.time4, a.time_over as timeOver, a.id_st1 as idSt1, a.id_st2 as idSt2, a.tpz, a.status, a.datetime, a.cat_pas as catPas, a.id_pas as idPas,
        COALESCE(p.full_name, 'ФИО не найдено') AS fullName, c.number 
        FROM applications a
        LEFT JOIN passenger p ON a.id_pas::bigint = p.id
        LEFT JOIN contact_numbers c ON p.id = c.passenger_id
        ORDER BY a.datetime DESC;
    """, nativeQuery = true)
    fun findAllApplicationsWithPassengerInfo(): List<ApplicationPassengerInfo>

    @Query("""
        SELECT a.id, a.time3, a.time4, a.time_over as timeOver, a.id_st1 as idSt1, a.id_st2 as idSt2, a.tpz, a.status, a.datetime, a.cat_pas as catPas, a.id_pas as idPas,
        COALESCE(p.full_name, 'ФИО не найдено') AS fullName, c.number 
        FROM applications a
        LEFT JOIN passenger p ON a.id_pas::bigint = p.id
        LEFT JOIN contact_numbers c ON p.id = c.passenger_id
        WHERE a.datetime BETWEEN :startOfDay AND :endOfDay
        AND LOWER(p.full_name) LIKE LOWER(CONCAT(:namePrefix, '%'))
        ORDER BY a.datetime DESC;
    """, nativeQuery = true)
    fun findApplicationsByDateAndPassengerNamePrefix(
        @Param("startOfDay") startOfDay: LocalDateTime,
        @Param("endOfDay") endOfDay: LocalDateTime,
        @Param("namePrefix") namePrefix: String
    ): List<ApplicationPassengerInfo>

    fun findAllByDatetimeBetween(from: Timestamp, to: Timestamp): List<Application>

}
