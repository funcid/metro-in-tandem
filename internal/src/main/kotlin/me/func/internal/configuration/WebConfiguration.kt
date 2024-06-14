package me.func.internal.configuration

import me.func.internal.properties.CorsProperties
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebConfiguration(
    private val corsProperties: CorsProperties
) : WebMvcConfigurer {

    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/**")
                .allowedOrigins(*corsProperties.allowedOrigins.toTypedArray())
                .allowedMethods(*corsProperties.allowedMethods.toTypedArray())
                .allowedHeaders(*corsProperties.allowedHeaders.toTypedArray())
                .allowCredentials(corsProperties.allowCredentials)
    }
}