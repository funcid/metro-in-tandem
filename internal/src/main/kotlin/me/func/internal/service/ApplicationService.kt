package me.func.internal.service

import RequestStatus
import me.func.internal.model.Application
import me.func.internal.repository.ApplicationRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class ApplicationService(private val applicationRepository: ApplicationRepository) {

    fun createApplication(application: Application): Application {
        return applicationRepository.save(application)
    }

    fun getApplication(id: String): Application? {
        return applicationRepository.findByIdOrNull(id)
    }

    fun updateApplication(id: String, application: Application): Application {
        if (!applicationRepository.existsById(id)) {
            throw NoSuchElementException("Application not found")
        }
        application.id = id
        return applicationRepository.save(application)
    }

    fun deleteApplication(id: String) {
        if (!applicationRepository.existsById(id)) {
            throw NoSuchElementException("Application not found")
        }
        applicationRepository.deleteById(id)
    }

    fun getAllApplications(): List<Application> {
        return applicationRepository.findAll().toList()
    }

    fun getApplicationsByStatus(status: RequestStatus): List<Application> {
        return applicationRepository.findByStatus(status)
    }
}