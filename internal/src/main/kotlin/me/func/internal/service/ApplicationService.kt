package me.func.internal.service

import jakarta.transaction.Transactional
import me.func.internal.dto.*
import me.func.internal.model.*
import java.sql.Timestamp
import me.func.internal.repository.ApplicationRepository
import me.func.internal.repository.MetroStationRepository
import me.func.internal.repository.PassengerRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import java.sql.Time
import java.time.LocalDate
import java.time.format.DateTimeFormatter

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

        // Получаем роль текущего пользователя
        val authentication = SecurityContextHolder.getContext().authentication
        val isEmployee = authentication.authorities.contains(SimpleGrantedAuthority("Сотрудник"))

        // Обновляем только статус, если пользователь является Сотрудником
        val updatedApplication = if (isEmployee) {
            existingApplication.copy(
                status = ApplicationStatus.fromCode(applicationUpdateRequest.status) ?: ApplicationStatus.NOT_APPROVED
            )
        } else {
            existingApplication.copy(
                status = ApplicationStatus.fromCode(applicationUpdateRequest.status) ?: ApplicationStatus.NOT_APPROVED,
                datetime = applicationUpdateRequest.datetime,
                time3 = applicationUpdateRequest.time3,
                time4 = applicationUpdateRequest.time4,
                inspSexM = applicationUpdateRequest.inspSexM,
                inspSexF = applicationUpdateRequest.inspSexF,
                idSt1 = applicationUpdateRequest.idSt1,
                idSt2 = applicationUpdateRequest.idSt2
            )
        }

        // Сохраняем обновленную заявку в репозитории
        return applicationRepository.save(updatedApplication)
    }

    fun deleteApplication(id: Long) {
        if (!applicationRepository.existsById(id)) {
            throw NoSuchElementException("Application not found")
        }
        applicationRepository.deleteById(id)
    }

    fun getApplicationsByDateAndPassengerNamePrefix(date: LocalDate, namePrefix: String): List<ApplicationResponse> {
        val startOfDay = date.atStartOfDay()
        val endOfDay = date.atTime(23, 59, 59)

        return applicationRepository.findApplicationsByDateAndPassengerNamePrefix(startOfDay, endOfDay, namePrefix).map { info ->
            ApplicationResponse(
                info.id,
                info.idPas,
                info.timeOver.toString(),
                info.status,
                info.datetime.toString(),
                info.fullName,
                info.number ?: "Мобильный номер отсутствует",
                MetroStation(
                    id = info.stationFromId.toString(),
                    nameStation = info.stationFromName,
                    nameLine = info.stationFromLine,
                    idLine = info.stationFromLineId.toString(),
                ),
                MetroStation(
                    id = info.stationToId.toString(),
                    nameStation = info.stationToName,
                    nameLine = info.stationToLine,
                    idLine = info.stationToLineId.toString(),
                ),
                info.employeeFio,
                info.employeeId
            )
        }
    }

    fun getApplicationsByStatus(status: ApplicationStatus): List<Application> {
        return applicationRepository.findByStatus(status)
    }
}