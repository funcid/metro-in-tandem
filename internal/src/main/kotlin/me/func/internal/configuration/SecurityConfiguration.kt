package me.func.internal.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
class SecurityConfiguration(
	private val userDetailsService: UserDetailsService,
	private val authEntryPoint: AuthEntryPointJwt
) {

	@Bean
	fun daoAuthenticationProvider(): DaoAuthenticationProvider {
		val authProvider = DaoAuthenticationProvider()
		authProvider.setUserDetailsService(userDetailsService)
		authProvider.setPasswordEncoder(passwordEncoder())
		return authProvider
	}

	@Bean
	fun authenticationManager(http: HttpSecurity): AuthenticationManager {
		val builder = http.getSharedObject(AuthenticationManagerBuilder::class.java)
		builder.authenticationProvider(daoAuthenticationProvider())
		return builder.build()
	}

	@Bean
	fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
		http
			.csrf { csrf -> csrf.disable() }
			.exceptionHandling { exceptionHandling -> exceptionHandling.authenticationEntryPoint(authEntryPoint) }
			.sessionManagement { sessionManagement ->
				sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			}
			.authorizeHttpRequests { auth ->
				auth.requestMatchers(
					"/auth/login",
					"/auth/register",
					"/actuator/**",
					"/swagger-ui/**",
					"/api-docs/**",
					"/api/v1/metro"
				).permitAll()
				auth.anyRequest().authenticated()
			}
			.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter::class.java)

		return http.build()
	}

	@Bean
	fun authenticationJwtTokenFilter(): AuthTokenFilter {
		return AuthTokenFilter()
	}

	@Bean
	fun passwordEncoder(): BCryptPasswordEncoder {
		return BCryptPasswordEncoder()
	}
}