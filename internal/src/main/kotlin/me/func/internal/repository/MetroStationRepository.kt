package me.func.internal.repository

import me.func.internal.model.MetroStation
import org.springframework.data.repository.CrudRepository
import org.springframework.data.rest.core.annotation.RepositoryRestResource

@RepositoryRestResource(collectionResourceRel = "metro_stations", path = "metro_stations")
interface MetroStationRepository : CrudRepository<MetroStation, String>