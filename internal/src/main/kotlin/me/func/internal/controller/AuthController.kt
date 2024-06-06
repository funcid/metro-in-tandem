package me.func.internal.controller

import me.func.internal.service.AuthService
import me.func.internal.model.LoginRequest
import me.func.internal.model.RegisterRequest
import me.func.internal.model.Role
import me.func.internal.model.User
import me.func.internal.service.UserService
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/auth")
class AuthController(
    private val authService: AuthService,
    private val userService: UserService,
    private val passwordEncoder: PasswordEncoder
) {

    @PostMapping("/login")
    fun login(@RequestBody loginRequest: LoginRequest): ResponseEntity<Any> {
        val jwt = authService.authenticate(loginRequest.username, loginRequest.password)
        return ResponseEntity.ok(mapOf("token" to jwt))
    }

    @PostMapping("/register")
    fun register(@RequestBody registerRequest: RegisterRequest): ResponseEntity<Any> {
        if (userService.existsByUsername(registerRequest.username)) {
            return ResponseEntity.badRequest().body("Username is already taken!")
        }

        val user = User(
            username = registerRequest.username,
            password = passwordEncoder.encode(registerRequest.password),
            role = Role(1, "USER") // предположим, что role_id 1 это "USER"
        )

        userService.save(user)
        return ResponseEntity.ok("User registered successfully!")
    }
}