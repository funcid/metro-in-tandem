package me.func.internal.controller

import me.func.internal.dto.ApplicationDetailsResponse
import me.func.internal.model.ApplicationStatus
import me.func.internal.dto.ApplicationResponse
import me.func.internal.dto.ApplicationUpdateRequest
import me.func.internal.dto.CreateApplicationRequest
import me.func.internal.model.Application
import me.func.internal.service.ApplicationService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/applications")
class ApplicationController(private val applicationService: ApplicationService) {

    @PostMapping
    fun createApplication(@RequestBody application: CreateApplicationRequest): ResponseEntity<Application> {
        val createdApplication = applicationService.createApplication(application)
        return ResponseEntity.status(HttpStatus.CREATED).body(createdApplication)
    }

    @GetMapping("/{id}")
    fun getApplication(@PathVariable id: Long): ResponseEntity<ApplicationDetailsResponse> {
        val application = applicationService.getApplication(id)
        return if (application != null) {
            ResponseEntity.ok(application)
        } else {
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(null)
        }
    }

    @PutMapping("/{id}")
    fun updateApplication(@PathVariable id: Long, @RequestBody application: ApplicationUpdateRequest): ResponseEntity<Application> {
        val updatedApplication = applicationService.updateApplication(id, application)
        return ResponseEntity.ok(updatedApplication)
    }

    @DeleteMapping("/{id}")
    fun deleteApplication(@PathVariable id: Long): ResponseEntity<Void> {
        applicationService.deleteApplication(id)
        return ResponseEntity.noContent().build()
    }

    @GetMapping
    fun getAllApplications(): ResponseEntity<List<ApplicationResponse>> {
        val applications = applicationService.getAllApplications()
        return ResponseEntity.ok(applications)
    }

    @GetMapping(params = ["status"])
    fun getApplicationsByStatus(@RequestParam status: ApplicationStatus): ResponseEntity<List<Application>> {
        val applications = applicationService.getApplicationsByStatus(status)
        return ResponseEntity.ok(applications)
    }
}