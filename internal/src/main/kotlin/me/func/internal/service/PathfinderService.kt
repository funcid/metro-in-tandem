package me.func.internal.service

import me.func.internal.model.MetroStation
import me.func.internal.util.findShortestPath
import me.func.internal.repository.MetroStationRepository
import me.func.internal.repository.MetroTimeRepository
import me.func.internal.repository.MetroTransferTimeRepository
import org.springframework.stereotype.Service

@Service
class PathfinderService(
    metroStationRepository: MetroStationRepository,
    metroTimeRepository: MetroTimeRepository,
    metroTransferTimeRepository: MetroTransferTimeRepository
) {

    private val metroStations = metroStationRepository.findAll().toList()
    private val metroTimes = metroTimeRepository.findAll().toList()
    private val metroTransferTimes = metroTransferTimeRepository.findAll().toList()

    fun findPath(
        startStationId: Int,
        endStationId: Int
    ): Pair<List<MetroStation>, Double> {
        val shortestPath = findShortestPath(
            startStationId,
            endStationId,
            metroStations,
            metroTimes,
            metroTransferTimes
        )

        var previousTime: Double? = null
        shortestPath.stations.forEachIndexed { index, metroStation ->
            previousTime = previousTime ?: 0.0
            val lastStation = shortestPath.stations[index - 1]
            val timeBetween = metroTimes.find {
                (it.idSt1 == lastStation.id && it.idSt2 == metroStation.id) ||
                        (it.idSt1 == metroStation.id && it.idSt2 == lastStation.id)
            }?.time ?: metroTransferTimes.find {
                (it.id1 == lastStation.id && it.id2 == metroStation.id) ||
                        (it.id1 == metroStation.id && it.id2 == lastStation.id)
            }?.time?.toDouble() ?: 0.0
            previousTime = (previousTime ?: 0.0) + timeBetween
        }
        return shortestPath.stations to String.format("%.1f", shortestPath.totalTime)
            .replace(',', '.')
            .toDouble()
    }
}