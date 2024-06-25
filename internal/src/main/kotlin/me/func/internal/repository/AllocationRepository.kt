package me.func.internal.repository

import me.func.internal.model.Allocation
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.sql.Timestamp

@Repository
interface AllocationRepository : JpaRepository<Allocation, Long> {

    fun findAllByEmployeeId(employeeId: Long): List<Allocation>

}