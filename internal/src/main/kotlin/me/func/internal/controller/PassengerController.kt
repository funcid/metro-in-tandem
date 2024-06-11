package me.func.internal.controller

import me.func.internal.dto.PassengerResponse
import me.func.internal.model.Passenger
import me.func.internal.service.PassengerService
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.net.URI

@RestController
@RequestMapping("/api/v1/passengers")
class PassengerController(private val service: PassengerService) {

    @PostMapping
    fun createPassenger(@RequestBody passenger: Passenger): ResponseEntity<Passenger> {
        val createdPassenger = service.createPassenger(passenger)
        val headers = HttpHeaders()
        headers.location = URI.create("/api/v1/passengers/${createdPassenger.id}")
        return ResponseEntity(createdPassenger, headers, HttpStatus.CREATED)
    }

    @GetMapping("/{id}")
    fun getPassenger(@PathVariable id: Long): ResponseEntity<Passenger> {
        val passenger = service.getPassengerById(id)
        return if (passenger != null) {
            ResponseEntity.ok(passenger)
        } else {
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(null)
        }
    }

    @PutMapping("/{id}")
    fun updatePassenger(@PathVariable id: Long, @RequestBody passenger: Passenger): ResponseEntity<Passenger> {
        val updatedPassenger = service.updatePassenger(id, passenger)
        return if (updatedPassenger != null) {
            ResponseEntity.ok(updatedPassenger)
        } else {
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(null)
        }
    }

    @DeleteMapping("/{id}")
    fun deletePassenger(@PathVariable id: Long): ResponseEntity<Void> {
        return if (service.deletePassenger(id)) {
            ResponseEntity.noContent().build()
        } else {
            ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        }
    }

    @GetMapping
    fun searchPassengers(@RequestParam search: String): ResponseEntity<List<Passenger>> {
        val passengers = service.searchPassengers(search)
        return ResponseEntity.ok(passengers)
    }

    @GetMapping("/all")
    fun getAllPassengers(): ResponseEntity<List<PassengerResponse>> {
        val passengers = service.getAllPassengers().map {
            PassengerResponse(
                id = it.id,
                fullName = it.fullName,
                contactNumbers = it.contactNumbers,
                gender = it.gender,
                category = it.category.categoryCode,
                additionalInfo = it.additionalInfo,
                hasPacemaker = it.hasPacemaker,
            )
        }
        return ResponseEntity.ok(passengers)
    }
}
