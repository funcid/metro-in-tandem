package me.func.internal.service

import me.func.internal.model.Passenger
import me.func.internal.repository.PassengerRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class PassengerService(private val repository: PassengerRepository) {

    fun createPassenger(passenger: Passenger): Passenger {
        return repository.save(passenger)
    }

    fun getPassengerById(id: Long): Passenger? {
        return repository.findById(id).orElse(null)
    }

    fun updatePassenger(id: Long, updatedPassenger: Passenger): Passenger? {
        return if (repository.existsById(id)) {
            updatedPassenger.copy(id = id)
            repository.save(updatedPassenger)
        } else {
            null
        }
    }

    fun deletePassenger(id: Long): Boolean {
        repository.deleteById(id)
        return true
    }

    fun searchPassengers(query: String): List<Passenger> {
        return repository.searchByFullNameOrCategory(query)
    }

    fun getAllPassengers(): List<Passenger>  {
        return repository.findAll()
    }
}