package me.func.internal;

import me.func.internal.model.RequestStatus
import com.fasterxml.jackson.databind.ObjectMapper
import me.func.internal.model.PassengerCategory
import me.func.internal.model.Application
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.sql.Time
import java.sql.Timestamp

class ApplicationE2ETests : AbstractTest() {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Test
    fun `E2E test for Application API`() {
        // Create an Application
        val application = Application(
            id = "test_id",
            idPas = "11058",
            datetime = Timestamp.valueOf("2024-04-24 07:30:00"),
            time3 = Time.valueOf("07:13:52"),
            time4 = Time.valueOf("07:51:11"),
            catPas = PassengerCategory.IZT,
            status = RequestStatus.REQUEST_COMPLETED,
                tpz = Timestamp.valueOf("2024-03-15 22:48:43"),
                inspSexM = 0,
                inspSexF = 1,
                timeOver = Time.valueOf("00:52:20"),
                idSt1 = "5",
                idSt2 = "97"
        )

        val applicationJson = objectMapper.writeValueAsString(application)

        val createResult = mockMvc.perform(post("/api/v1/applications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(applicationJson))
                .andExpect(status().isCreated)
                .andExpect(jsonPath("$.id").value("test_id"))
                .andExpect(jsonPath("$.id_pas").value("11058"))
                .andReturn()

        val createdApplicationId = objectMapper
                .readTree(createResult.response.contentAsString)
                .get("id")
                .asText()

        // Get the created Application
        mockMvc.perform(get("/api/v1/applications/{id}", createdApplicationId))
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.id").value(createdApplicationId))
                .andExpect(jsonPath("$.id_pas").value("11058"))

        // Update the Application
        val updatedApplication = application.copy(idPas = "11059")
        val updatedApplicationJson = objectMapper.writeValueAsString(updatedApplication)

        mockMvc.perform(put("/api/v1/applications/{id}", createdApplicationId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedApplicationJson))
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.id_pas").value("11059"))

        // Search for the Application by status
        mockMvc.perform(get("/api/v1/applications").param("status", RequestStatus.REQUEST_COMPLETED.name))
                .andExpect(status().isOk)
                .andExpect(jsonPath("$[0].id_pas").value("11059"))

        // Delete the Application
        mockMvc.perform(delete("/api/v1/applications/{id}", createdApplicationId))
                .andExpect(status().isNoContent)

        // Confirm the Application is deleted
        mockMvc.perform(get("/api/v1/applications/{id}", createdApplicationId))
                .andExpect(status().isNotFound)
    }
}