package me.func.internal.service

import me.func.internal.configuration.JwtUtils
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val authenticationManager: AuthenticationManager,
    private val jwtUtils: JwtUtils
) {

    fun authenticate(username: String, password: String): Pair<String, String?> {
        val authentication: Authentication = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(username, password)
        )
        return jwtUtils.generateJwtToken(authentication.name) to authentication.authorities.firstOrNull()?.authority
    }
}