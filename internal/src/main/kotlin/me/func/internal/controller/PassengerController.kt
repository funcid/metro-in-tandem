package me.func.internal.controller

import me.func.internal.dto.PassengerDetailsResponse
import me.func.internal.dto.PassengerResponse
import me.func.internal.model.Passenger
import me.func.internal.service.PassengerService
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.net.URI

@RestController
@RequestMapping("/api/v1/passengers")
class PassengerController(private val service: PassengerService) {

    @PostMapping
    @PreAuthorize("hasAnyAuthority('Администратор', 'Специалист', 'Оператор')")
    fun createPassenger(@RequestBody passenger: Passenger): ResponseEntity<Passenger> {
        val createdPassenger = service.createPassenger(passenger)
        return ResponseEntity(createdPassenger, HttpStatus.CREATED)
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('Администратор', 'Специалист', 'Оператор')")
    fun getPassenger(@PathVariable id: Long): ResponseEntity<PassengerDetailsResponse> {
        val passenger = service.getPassengerById(id) ?: return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null)
        val response = PassengerDetailsResponse(
            fullName = passenger.fullName,
            contactNumbers = passenger.contactNumbers,
            gender = passenger.gender,
            category = passenger.category.categoryCode,
            additionalInfo = passenger.additionalInfo,
            hasPacemaker = passenger.hasPacemaker
        )
        return ResponseEntity.ok(response)
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('Администратор', 'Специалист', 'Оператор')")
    fun updatePassenger(@PathVariable id: Long, @RequestBody passenger: Passenger): ResponseEntity<Passenger> {
        val updatedPassenger = service.updatePassenger(id, passenger)
        return if (updatedPassenger != null) {
            ResponseEntity.ok(updatedPassenger)
        } else {
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(null)
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('Администратор')")
    fun deletePassenger(@PathVariable id: Long): ResponseEntity<Void> {
        return if (service.deletePassenger(id)) {
            ResponseEntity.noContent().build()
        } else {
            ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        }
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('Администратор', 'Специалист', 'Оператор')")
    fun searchPassengers(@RequestParam search: String): ResponseEntity<List<Passenger>> {
        val passengers = service.searchPassengers(search)
        return ResponseEntity.ok(passengers)
    }

    @GetMapping("/all")
    @PreAuthorize("hasAnyAuthority('Администратор', 'Специалист', 'Оператор')")
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
