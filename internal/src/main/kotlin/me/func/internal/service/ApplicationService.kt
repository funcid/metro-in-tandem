package me.func.internal.service

import jakarta.transaction.Transactional
import me.func.internal.dto.ApplicationDetailsResponse
import me.func.internal.model.ApplicationStatus
import me.func.internal.dto.ApplicationResponse
import me.func.internal.dto.ApplicationUpdateRequest
import me.func.internal.dto.CreateApplicationRequest
import me.func.internal.model.Application
import me.func.internal.model.Passenger
import me.func.internal.model.PassengerCategory
import java.sql.Timestamp
import me.func.internal.repository.ApplicationRepository
import me.func.internal.repository.MetroStationRepository
import me.func.internal.repository.PassengerRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.sql.Time

@Service
@Transactional
class ApplicationService(
    private val applicationRepository: ApplicationRepository,
    private val pathfinderService: PathfinderService,
    private val passengerRepository: PassengerRepository,
    private val stationRepository: MetroStationRepository
) {

    fun createApplication(application: CreateApplicationRequest): Application? {
        val passenger = passengerRepository.findByIdOrNull(application.idPas.toLong()) ?: return null

        val newApplication = Application(
            idPas = application.idPas,
            datetime = application.datetime,
            status = ApplicationStatus.NOT_APPROVED,
            inspSexF = application.inspSexF,
            inspSexM = application.inspSexM,
            idSt1 =  application.idSt1.toString(),
            idSt2 = application.idSt2.toString(),
            time4 = Time.valueOf(application.datetime.toLocalDateTime().toLocalTime()),
            time3 = Time.valueOf(application.datetime.toLocalDateTime().toLocalTime()),
            timeOver = Time(System.currentTimeMillis()),
            tpz = Timestamp(System.currentTimeMillis()),
            catPas = passenger.category,
        )
        return applicationRepository.save(newApplication)
    }

    fun getApplication(id: Long): ApplicationDetailsResponse? {
        val application = applicationRepository.findByIdOrNull(id) ?: return null
        val passenger = passengerRepository.findByIdOrNull(application.idPas.toLong()) ?: Passenger(
            id = -1,
            fullName = "Пассажир не привязан",
            contactNumbers = emptySet(),
            gender = "Мужской",
            category = PassengerCategory.NO,
            additionalInfo = "Пассажир не привязан к заявке, удалите и создайте заново",
            hasPacemaker = false
        )
        val (path, duration) = pathfinderService.findPath(application.idSt1.toInt(), application.idSt2.toInt())

        return applicationRepository.findByIdOrNull(id).run {
            ApplicationDetailsResponse(
                idPas = application.idPas,
                time3 = application.time3,
                time4 = application.time4,
                timeOver = application.timeOver,
                status = application.status.statusCode,
                catPas = passenger.category.categoryCode,
                datetime = application.datetime,
                fio = passenger.fullName,
                stationFrom = stationRepository.findByIdOrNull(application.idSt1),
                stationTo = stationRepository.findByIdOrNull(application.idSt2),
                duration = "$duration мин.",
                inspSexF = application.inspSexF,
                inspSexM = application.inspSexM,
                tpz = application.tpz,
                transplants = path.distinctBy { it.idLine }.count() - 1,
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
                info.idPas,
                info.time3.toString(),
                info.time4.toString(),
                info.timeOver.toString(),
                info.status,
                info.catPas,
                info.datetime.toString(),
                info.fullName,
                info.number ?: "Мобильный номер отсутствует",
                info.tpz.toString(),
                stationRepository.findByIdOrNull(info.idSt1),
                stationRepository.findByIdOrNull(info.idSt2),
            )
        }
    }

    fun getApplicationsByStatus(status: ApplicationStatus): List<Application> {
        return applicationRepository.findByStatus(status)
    }
}