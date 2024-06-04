package me.func.internal.repository

import me.func.internal.model.BidCancellation
import org.springframework.data.repository.CrudRepository
import org.springframework.data.rest.core.annotation.RepositoryRestResource

@RepositoryRestResource(collectionResourceRel = "bid_cancellations", path = "bid_cancellations")
interface BidCancellationRepository : CrudRepository<BidCancellation, String>