package me.func.internal.repository

import me.func.internal.model.MetroTime
import org.springframework.data.repository.CrudRepository
import org.springframework.data.rest.core.annotation.RepositoryRestResource
import java.math.BigDecimal

@RepositoryRestResource(collectionResourceRel = "metro_times", path = "metro_times")
interface MetroTimeRepository : CrudRepository<MetroTime, BigDecimal>