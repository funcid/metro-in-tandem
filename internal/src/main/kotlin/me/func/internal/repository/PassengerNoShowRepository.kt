package me.func.internal.repository

import me.func.internal.model.PassengerNoShow
import org.springframework.data.repository.CrudRepository
import org.springframework.data.rest.core.annotation.RepositoryRestResource

@RepositoryRestResource(collectionResourceRel = "passenger_no_show", path = "passenger_no_show")
interface PassengerNoShowRepository : CrudRepository<PassengerNoShow, String>