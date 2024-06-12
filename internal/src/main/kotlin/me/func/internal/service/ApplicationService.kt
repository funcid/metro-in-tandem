package me.func.internal.service

import me.func.internal.model.RequestStatus
import me.func.internal.dto.ApplicationResponse
import me.func.internal.model.Application
import me.func.internal.repository.ApplicationRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class ApplicationService(
    private val applicationRepository: ApplicationRepository
) {

    fun createApplication(application: Application): Application {
        return applicationRepository.save(application)
    }

    fun getApplication(id: Long): Application? {
        return applicationRepository.findByIdOrNull(id)
    }

    fun updateApplication(id: Long, application: Application): Application {
        if (!applicationRepository.existsById(id)) {
            throw NoSuchElementException("Application not found")
        }
        application.id = id
        return applicationRepository.save(application)
    }

    fun deleteApplication(id: Long) {
        if (!applicationRepository.existsById(id)) {
            throw NoSuchElementException("Application not found")
        }
        applicationRepository.deleteById(id)
    }

    fun getAllApplications(): List<ApplicationResponse> {
        return applicationRepository.findAllApplicationsWithPassengerInfo().map { info ->
            ApplicationResponse(
                info.id,
                info.time3.toString(),
                info.time4.toString(),
                info.timeOver.toString(),
                info.status ?: "Нет статуса",
                info.catPas,
                info.datetime.toString(),
                info.fullName,
                info.number ?: "Мобильный номер отсутствует",
            )
        }
    }

    fun getApplicationsByStatus(status: RequestStatus): List<Application> {
        return applicationRepository.findByStatus(status)
    }
}