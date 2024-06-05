package me.func.internal.repository

import me.func.internal.model.Passenger
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface PassengerRepository : JpaRepository<Passenger, Long> {

    @Query("SELECT p FROM Passenger p WHERE p.fullName LIKE %:query% OR p.category LIKE %:query%")
    fun searchByFullNameOrCategory(query: String): List<Passenger>

}
