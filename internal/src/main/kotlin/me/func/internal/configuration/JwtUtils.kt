package me.func.internal.configuration

import io.jsonwebtoken.*
import org.springframework.stereotype.Component
import java.util.Date

@Component
class JwtUtils {
    private val jwtSecret = "secretKey" // Желательно хранить секретный ключ в конфигурации
    private val jwtExpirationMs: Long = 86400000 // 1 день

    fun generateJwtToken(username: String): String {
        return Jwts.builder()
            .setSubject(username)
            .setIssuedAt(Date())
            .setExpiration(Date(System.currentTimeMillis() + jwtExpirationMs))
            .signWith(SignatureAlgorithm.HS512, jwtSecret)
            .compact()
    }

    fun getUserNameFromJwtToken(token: String): String {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).body.subject
    }

    fun validateJwtToken(authToken: String): Boolean {
        return try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken)
            true
        } catch (e: Exception) {
            false
        }
    }
}