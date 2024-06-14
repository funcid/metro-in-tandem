package me.func.internal.service

import me.func.internal.model.Passenger
import me.func.internal.repository.PassengerRepository
import org.springframework.data.repository.findByIdOrNull
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
        val passenger = repository.findByIdOrNull(id)?.apply {
            this.fullName = updatedPassenger.fullName
            this.category  = updatedPassenger.category
            this.gender = updatedPassenger.gender
            this.hasPacemaker = updatedPassenger.hasPacemaker
            this.additionalInfo = updatedPassenger.additionalInfo
            this.contactNumbers = updatedPassenger.contactNumbers
        } ?: return null
        return repository.save(passenger)
    }

    fun deletePassenger(id: Long): Boolean {
        repository.deleteById(id)
        return true
    }

    fun searchPassengers(query: String): List<Passenger> {
        return repository.searchByFullNameOrCategory(query)
    }

    fun getAllPassengers(): List<Passenger> {
        return repository.findAll()
    }
}