package me.func.internal.repository

import me.func.internal.model.Application
import org.springframework.data.repository.CrudRepository
import org.springframework.data.rest.core.annotation.RepositoryRestResource

@RepositoryRestResource(collectionResourceRel = "applications", path = "applications")
interface ApplicationRepository : CrudRepository<Application, String>
