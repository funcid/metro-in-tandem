package me.func.internal.controller

import me.func.internal.dto.*
import me.func.internal.model.Application
import me.func.internal.model.ApplicationStatus
import me.func.internal.service.ApplicationService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.time.LocalDate

@RestController
@RequestMapping("/api/v1/applications")
class ApplicationController(private val applicationService: ApplicationService) {

    @PostMapping
    @PreAuthorize("hasAnyAuthority('Администратор', 'Специалист', 'Оператор')")
    fun createApplication(@RequestBody application: CreateApplicationRequest): ResponseEntity<Application> {
        val createdApplication = applicationService.createApplication(application)
        return ResponseEntity.status(HttpStatus.CREATED).body(createdApplication)
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('Администратор', 'Специалист', 'Оператор', 'Сотрудник')")
    fun getApplication(@PathVariable id: Long): ResponseEntity<ApplicationDetailsResponse> {
        val application = applicationService.getApplication(id)
        return if (application != null) {
            ResponseEntity.ok(application)
        } else {
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(null)
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('Администратор', 'Специалист', 'Оператор', 'Сотрудник')")
    fun updateApplication(@PathVariable id: Long, @RequestBody application: ApplicationUpdateRequest): ResponseEntity<Application> {
        // todo: сотрудник может менять только статус
        val updatedApplication = applicationService.updateApplication(id, application)
        return ResponseEntity.ok(updatedApplication)
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('Администратор')")
    fun deleteApplication(@PathVariable id: Long): ResponseEntity<Void> {
        applicationService.deleteApplication(id)
        return ResponseEntity.noContent().build()
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('Администратор', 'Специалист', 'Оператор')")
    fun getAllApplications(): ResponseEntity<List<ApplicationResponse>> {
        val applications = applicationService.getAllApplications()
        return ResponseEntity.ok(applications)
    }

    @GetMapping(params = ["status"])
    @PreAuthorize("hasAnyAuthority('Администратор', 'Специалист', 'Оператор')")
    fun getApplicationsByStatus(@RequestParam status: ApplicationStatus): ResponseEntity<List<Application>> {
        val applications = applicationService.getApplicationsByStatus(status)
        return ResponseEntity.ok(applications)
    }

    @GetMapping("/search")
    @PreAuthorize("hasAnyAuthority('Администратор', 'Специалист', 'Оператор')")
    fun getApplicationsByDateAndPassengerNamePrefix(
        @RequestParam date: String,    // Параметр даты в виде строки
        @RequestParam namePrefix: String
    ): ResponseEntity<List<ApplicationPassengerInfo>> {
        // Преобразуем строку даты в LocalDate
        val localDate = LocalDate.parse(date)
        val applications = applicationService.getApplicationsByDateAndPassengerNamePrefix(localDate, namePrefix)
        return ResponseEntity.ok(applications)
    }
}