package me.func.internal.controller

import me.func.internal.model.MetroStation
import me.func.internal.service.MetroService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/metro")
class MetroController(private val metroService: MetroService) {

    @GetMapping
    fun getAllMetroStations(): ResponseEntity<Iterable<MetroStation>> {
        return ResponseEntity.ok(metroService.fetchAllMetroStations())
    }
}