package me.func.internal.service

import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class CustomUserDetailsService(private val userService: UserService) : UserDetailsService {

  override fun loadUserByUsername(username: String): UserDetails {
    val user = userService.findByUsername(username)
      ?: throw UsernameNotFoundException("User not found with username: $username")

    return org.springframework.security.core.userdetails.User(
      user.username, user.password, listOf(SimpleGrantedAuthority(user.role.name))
    )
  }
}