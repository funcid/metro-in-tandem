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
    SELECT 
        a.id, 
        a.id_pas as idPas,
        a.time3,
        a.time4,
        a.time_over as timeOver, 
        a.status, 
        a.cat_pas as catPas,
        a.datetime, 
        COALESCE(p.full_name, 'ФИО не найдено') AS fullName, 
        c.number as mobileNumber,
        a.tpz,
        ms1.id as stationFromId,
        ms1.name_station as stationFromName,
        ms1.name_line as stationFromLine,
        ms1.id_line as stationFromLineId,
        ms2.id as stationToId,
        ms2.name_station as stationToName,
        ms2.name_line as stationToLine,
        ms2.id_line as stationToLineId,
        alloc.employee_fio as employeeFio, 
        alloc.employee_id as employeeId
    FROM 
        applications a
    LEFT JOIN 
        passenger p ON a.id_pas::bigint = p.id
    LEFT JOIN 
        contact_numbers c ON p.id = c.passenger_id
    LEFT JOIN 
        allocation alloc ON a.id = alloc.application_id
    LEFT JOIN 
        metro_stations ms1 ON a.id_st1 = ms1.id
    LEFT JOIN 
        metro_stations ms2 ON a.id_st2 = ms2.id
    WHERE 
        (:namePrefix IS NULL OR LOWER(COALESCE(p.full_name, 'ФИО не найдено')) LIKE LOWER(CONCAT(:namePrefix, '%')))
        AND a.datetime BETWEEN :startOfDay AND :endOfDay
        AND alloc.type = 'APPLICATION'
    ORDER BY 
        a.datetime DESC
""", nativeQuery = true)
    fun findApplicationsByDateAndPassengerNamePrefix(
        @Param("startOfDay") startOfDay: LocalDateTime,
        @Param("endOfDay") endOfDay: LocalDateTime,
        @Param("namePrefix") namePrefix: String
    ): List<ApplicationPassengerInfo>


    fun findAllByDatetimeBetween(from: Timestamp, to: Timestamp): List<Application>

}
