package me.func.internal.service

import me.func.internal.model.User
import me.func.internal.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class UserService(private val userRepository: UserRepository) {

    fun existsByUsername(username: String): Boolean {
        return userRepository.existsByUsername(username)
    }

    fun save(user: User): User {
        return userRepository.save(user)
    }

    fun findByUsername(username: String): User? {
        return userRepository.findByUsername(username)
    }
}