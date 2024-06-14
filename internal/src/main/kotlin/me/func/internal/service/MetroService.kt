package me.func.internal.service

import me.func.internal.model.MetroStation
import me.func.internal.repository.MetroStationRepository
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service

@Service
class MetroService(private val metroStationRepository: MetroStationRepository) {

    var metroStations: Iterable<MetroStation>? = null;

    fun fetchAllMetroStations(): Iterable<MetroStation> {
        return metroStations ?: metroStationRepository.findAll(
            Sort.by(
                Sort.Direction.ASC,
                "nameStation"
            )
        ).apply { metroStations = this }
    }
}