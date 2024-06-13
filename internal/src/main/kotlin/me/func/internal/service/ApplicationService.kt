package me.func.internal.service

import jakarta.transaction.Transactional
import me.func.internal.dto.ApplicationDetailsResponse
import me.func.internal.model.ApplicationStatus
import me.func.internal.dto.ApplicationResponse
import me.func.internal.dto.ApplicationUpdateRequest
import me.func.internal.model.Application
import me.func.internal.repository.ApplicationRepository
import me.func.internal.repository.MetroStationRepository
import me.func.internal.repository.PassengerRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
@Transactional
class ApplicationService(
    private val applicationRepository: ApplicationRepository,
    private val pathfinderService: PathfinderService,
    private val passengerRepository: PassengerRepository,
    private val stationRepository: MetroStationRepository
) {

    fun createApplication(application: Application): Application {
        return applicationRepository.save(application)
    }

    fun getApplication(id: Long): ApplicationDetailsResponse? {
        val application = applicationRepository.findByIdOrNull(id) ?: return null
        val passenger = passengerRepository.findByIdOrNull(application.idPas.toLong()) ?: return null
        val calculatedDuration = pathfinderService.findPath(application.idSt1.toInt(), application.idSt2.toInt()).second

        return applicationRepository.findByIdOrNull(id).run {
            ApplicationDetailsResponse(
                idPas = application.idPas,
                time3 = application.time3,
                time4 = application.time4,
                timeOver = application.timeOver,
                status = application.status!!.statusCode,
                catPas = passenger.category.categoryCode,
                datetime = application.datetime,
                fio = passenger.fullName,
                idSt1 = application.idSt1,
                station1Name = stationRepository.findByIdOrNull(application.idSt1)?.nameStation ?: "null",
                idSt2 = application.idSt2,
                station2Name = stationRepository.findByIdOrNull(application.idSt2)?.nameStation ?: "null",
                duration = "$calculatedDuration мин.",
                inspSexF = application.inspSexF,
                inspSexM = application.inspSexM,
                tpz = application.tpz
            )
        }
    }

    fun updateApplication(id: Long, applicationUpdateRequest: ApplicationUpdateRequest): Application {
        val existingApplication = applicationRepository.findById(id)
            .orElseThrow { NoSuchElementException("Application not found") }

        // Обновляем свойства заявки значениями из ApplicationUpdateRequest
        val updatedApplication = existingApplication.copy(
            status = ApplicationStatus.fromCode(applicationUpdateRequest.status) ?: ApplicationStatus.NOT_APPROVED,
            datetime = applicationUpdateRequest.datetime,
            time3 = applicationUpdateRequest.time3,
            time4 = applicationUpdateRequest.time4,
            inspSexM = applicationUpdateRequest.inspSexM,
            inspSexF = applicationUpdateRequest.inspSexF,
            idSt1 = applicationUpdateRequest.idSt1,
            idSt2 = applicationUpdateRequest.idSt2
        )

        // Сохраняем обновленную заявку в репозитории
        return applicationRepository.save(updatedApplication)
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
                info.status,
                info.catPas,
                info.datetime.toString(),
                info.fullName,
                info.number ?: "Мобильный номер отсутствует",
            )
        }
    }

    fun getApplicationsByStatus(status: ApplicationStatus): List<Application> {
        return applicationRepository.findByStatus(status)
    }
}