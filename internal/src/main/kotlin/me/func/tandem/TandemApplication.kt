package me.func.tandem

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class TandemApplication

fun main(args: Array<String>) {
	runApplication<TandemApplication>(*args)
}
