package me.func.internal.repository

import me.func.internal.model.MetroTransferTime
import org.springframework.data.repository.CrudRepository
import org.springframework.data.rest.core.annotation.RepositoryRestResource
import java.math.BigDecimal

@RepositoryRestResource(collectionResourceRel = "metro_transfer_times", path = "metro_transfer_times")
interface MetroTransferTimeRepository : CrudRepository<MetroTransferTime, BigDecimal>