package me.func.internal

import com.fasterxml.jackson.databind.ObjectMapper
import me.func.internal.model.Employee
import me.func.internal.service.EmployeeService
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.time.LocalDate
import java.util.*

class EmployeeControllerTest : AbstractTest() {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var employeeService: EmployeeService

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Test
    fun `should return employee by id`() {
        val employee = Employee(
            id = "1",
            date = LocalDate.of(2023, 10, 15),
            timeWork = "08:00-17:00",
            fio = "John Doe",
            uchastok = "North",
            smena = "Day",
            rank = "Developer",
            sex = "Male"
        )
        Mockito.`when`(employeeService.getEmployeeById("1")).thenReturn(Optional.of(employee))

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/employees/1"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.id").value("1"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.date").value("2023-10-15"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.timeWork").value("08:00-17:00"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.fio").value("John Doe"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.uchastok").value("North"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.smena").value("Day"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.rank").value("Developer"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.sex").value("Male"))
    }

    @Test
    fun `should return not found for non-existing employee`() {
        Mockito.`when`(employeeService.getEmployeeById("2")).thenReturn(Optional.empty())

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/employees/2"))
            .andExpect(MockMvcResultMatchers.status().isNotFound)
    }

    @Test
    fun `should update employee`() {
        val updatedEmployee = Employee(
            id = "1",
            date = LocalDate.of(2023, 10, 15),
            timeWork = "08:00-17:00",
            fio = "John Doe Updated",
            uchastok = "North",
            smena = "Night",
            rank = "Senior Developer",
            sex = "Male"
        )
        Mockito.`when`(employeeService.updateEmployee("1", updatedEmployee)).thenReturn(updatedEmployee)

        mockMvc.perform(
            MockMvcRequestBuilders.put("/api/v1/employees/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedEmployee))
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.fio").value("John Doe Updated"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.smena").value("Night"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.rank").value("Senior Developer"))
    }

    @Test
    fun `should return not found when updating non-existing employee`() {
        val updatedEmployee = Employee(
            id = "2",
            date = LocalDate.of(2023, 10, 15),
            timeWork = "08:00-17:00",
            fio = "Jane Doe",
            uchastok = "South",
            smena = "Day",
            rank = "Developer",
            sex = "Female"
        )
        Mockito.doThrow(NoSuchElementException("Employee with id 2 not found"))
            .`when`(employeeService).updateEmployee("2", updatedEmployee)

        mockMvc.perform(
            MockMvcRequestBuilders.put("/api/v1/employees/2")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedEmployee))
        )
            .andExpect(MockMvcResultMatchers.status().isNotFound)
    }

    @Test
    fun `should return employees by region`() {
        val employees = listOf(Employee(
            id = "1",
            date = LocalDate.of(2023, 10, 15),
            timeWork = "08:00-17:00",
            fio = "John Doe",
            uchastok = "North",
            smena = "Day",
            rank = "Developer",
            sex = "Male"
        ))
        Mockito.`when`(employeeService.getEmployeesByRegion("North")).thenReturn(employees)

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/employees").param("region", "North"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value("1"))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].fio").value("John Doe"))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].uchastok").value("North"))
    }

    @Test
    fun `should return no content when no employees found in region`() {
        Mockito.`when`(employeeService.getEmployeesByRegion("South")).thenReturn(emptyList())

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/employees").param("region", "South"))
            .andExpect(MockMvcResultMatchers.status().isNoContent)
    }
}