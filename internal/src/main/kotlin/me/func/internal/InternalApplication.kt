package me.func.internal

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@ConfigurationPropertiesScan
@SpringBootApplication
class InternalApplication

fun main(args: Array<String>) {
	runApplication<InternalApplication>(*args)
}
