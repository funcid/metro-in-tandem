package me.func.internal.repository

import me.func.internal.model.Employee
import org.springframework.data.repository.CrudRepository
import org.springframework.data.rest.core.annotation.RepositoryRestResource

@RepositoryRestResource(collectionResourceRel = "employees", path = "employees")
interface EmployeeRepository : CrudRepository<Employee, String> {

    fun findByUchastok(uchastok: String): List<Employee>

}