package me.func.internal.repository

import me.func.internal.model.TimeChange
import org.springframework.data.repository.CrudRepository
import org.springframework.data.rest.core.annotation.RepositoryRestResource

@RepositoryRestResource(collectionResourceRel = "time_changes", path = "time_changes")
interface TimeChangeRepository : CrudRepository<TimeChange, String>