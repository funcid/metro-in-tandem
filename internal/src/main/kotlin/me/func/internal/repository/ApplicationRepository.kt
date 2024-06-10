package me.func.internal.repository

import me.func.internal.dto.RequestStatus
import me.func.internal.dto.ApplicationPassengerInfo
import me.func.internal.model.Application
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.rest.core.annotation.RepositoryRestResource

@RepositoryRestResource(collectionResourceRel = "applications", path = "applications")
interface ApplicationRepository : CrudRepository<Application, String> {

    fun findByStatus(status: RequestStatus): List<Application>

    @Query("""
        SELECT a.id, a.time3, a.time4, a.time_over as timeOver, a.status, a.datetime, a.cat_pas as catPas, 
        COALESCE(p.full_name, 'ФИО не найдено') AS fullName, c.number 
        FROM applications a
        LEFT JOIN passenger p ON a.id_pas::bigint = p.id
        LEFT JOIN contact_numbers c ON p.id = c.passenger_id
        ORDER BY a.datetime DESC;
    """, nativeQuery = true)
    fun findAllApplicationsWithPassengerInfo(): List<ApplicationPassengerInfo>

}
