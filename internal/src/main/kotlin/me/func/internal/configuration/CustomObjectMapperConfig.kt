package me.func.internal.configuration

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile

@Configuration
class CustomObjectMapperConfig {

    @Bean
    fun objectMapper(): ObjectMapper {
        return jacksonObjectMapper().registerModule(KotlinModule())
    }
}