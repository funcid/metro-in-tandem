package me.func.internal.repository

import me.func.internal.model.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, Long> {

    fun existsByUsername(username: String): Boolean

    fun findByUsername(username: String): User?

}