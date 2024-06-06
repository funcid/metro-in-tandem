package me.func.internal.util

import me.func.internal.model.MetroStation
import me.func.internal.model.MetroTime
import me.func.internal.model.MetroTransferTime
import java.util.PriorityQueue

data class Station(val metroStation: MetroStation, val distance: Double, val path: List<MetroStation>)

val adjacencyList = mutableMapOf<String, MutableList<Pair<String, Double>>>()

fun findShortestPath(
    startStationId: Int,
    endStationId: Int,
    stations: List<MetroStation>,
    times: List<MetroTime>,
    transferTimes: List<MetroTransferTime>
): PathResult {
    val stationMap = stations.associateBy { it.id }

    if (adjacencyList.isEmpty()) {
        times.forEach {
            adjacencyList.computeIfAbsent(it.idSt1) { mutableListOf() }.add(Pair(it.idSt2, it.time))
            adjacencyList.computeIfAbsent(it.idSt2) { mutableListOf() }.add(Pair(it.idSt1, it.time))
        }

        transferTimes.forEach {
            adjacencyList.computeIfAbsent(it.id1) { mutableListOf() }.add(Pair(it.id2, it.time.toDouble()))
            adjacencyList.computeIfAbsent(it.id2) { mutableListOf() }.add(Pair(it.id1, it.time.toDouble()))
        }
    }

    val pq = PriorityQueue<Station>(compareBy { it.distance })
    val distances = mutableMapOf<String, Double>()
    val previous = mutableMapOf<String, String>()

    stationMap.forEach { (id, _) ->
        distances[id] = Double.MAX_VALUE
    }
    distances[startStationId.toString()] = 0.0

    pq.add(Station(stationMap[startStationId.toString()]!!, 0.0, listOf()))

    while (pq.isNotEmpty()) {
        val currentStation = pq.poll()

        if (currentStation.metroStation.id == endStationId.toString()) {
            return PathResult(
                stations = currentStation.path + stationMap[endStationId.toString()]!!,
                totalTime = currentStation.distance
            )
        }

        val currentDistance = distances[currentStation.metroStation.id]!!

        adjacencyList[currentStation.metroStation.id]?.forEach { (neighborId, weight) ->
            val distance = currentDistance + weight
            if (distance < distances[neighborId]!!) {
                distances[neighborId] = distance
                previous[neighborId] = currentStation.metroStation.id
                pq.add(Station(stationMap[neighborId]!!, distance, currentStation.path + currentStation.metroStation))
            }
        }
    }

    throw Exception("Path not found")
}

data class PathResult(
    val stations: List<MetroStation>,
    val totalTime: Double
)
