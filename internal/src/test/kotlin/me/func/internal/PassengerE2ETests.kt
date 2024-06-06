import com.fasterxml.jackson.databind.ObjectMapper
import me.func.internal.InternalApplication
import me.func.internal.dto.PassengerCategory
import me.func.internal.model.ContactNumber
import me.func.internal.model.Passenger
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@DirtiesContext
@ActiveProfiles("test")
@ExtendWith(SpringExtension::class)
@SpringBootTest(
    classes = [InternalApplication::class],
    webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,
)
@AutoConfigureMockMvc
class PassengerE2ETests {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Test
    fun `E2E test for Passenger API`() {
        // Create a Passenger
        val passenger = Passenger(
            fullName = "John Doe",
            contactNumbers = setOf(ContactNumber(number = "1234567890", description = "Home")),
            gender = "Male",
            category = PassengerCategory.PL,
            additionalInfo = "VIP",
            hasPacemaker = false
        )

        val passengerJson = objectMapper.writeValueAsString(passenger)

        val createResult = mockMvc.perform(post("/api/v1/passengers")
            .contentType(MediaType.APPLICATION_JSON)
            .content(passengerJson))
            .andExpect(status().isCreated)
            .andExpect(jsonPath("$.id").isNumber)
            .andExpect(jsonPath("$.fullName").value("John Doe"))
            .andReturn()

        val createdPassengerId = objectMapper
            .readTree(createResult.response.contentAsString)
            .get("id")
            .asLong()

        // Get the created Passenger
        mockMvc.perform(get("/api/v1/passengers/{id}", createdPassengerId))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(createdPassengerId))
            .andExpect(jsonPath("$.fullName").value("John Doe"))

        // Update the Passenger
        val updatedPassenger = passenger.copy(fullName = "John Smith")
        val updatedPassengerJson = objectMapper.writeValueAsString(updatedPassenger)

        mockMvc.perform(put("/api/v1/passengers/{id}", createdPassengerId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(updatedPassengerJson))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.fullName").value("John Smith"))

        // Search for the Passenger
        mockMvc.perform(get("/api/v1/passengers")
            .param("search", "John Smith"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$[0].fullName").value("John Smith"))

        // Delete the Passenger
        mockMvc.perform(delete("/api/v1/passengers/{id}", createdPassengerId))
            .andExpect(status().isNoContent)

        // Confirm the Passenger is deleted
        mockMvc.perform(get("/api/v1/passengers/{id}", createdPassengerId))
            .andExpect(status().isNotFound)
    }
}