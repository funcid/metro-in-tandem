package me.func.internal

import com.fasterxml.jackson.databind.ObjectMapper
import me.func.internal.model.LoginRequest
import me.func.internal.model.RegisterRequest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class AuthControllerTest : AbstractTest() {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @BeforeEach
    fun setup() {
        val signUpRequest = RegisterRequest(
            username = "newuser",
            password = "password123"
        )

        mockMvc.perform(
            MockMvcRequestBuilders.post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signUpRequest))
        )
    }

    @Test
    fun `login with created user`() {
        val loginRequest = LoginRequest(
            username = "newuser",
            password = "password123"
        )

        mockMvc.perform(
            MockMvcRequestBuilders.post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest))
        )
            .andExpect(status().isOk)
            .andExpect { result ->
                assert(result.response.contentAsString.contains("token"))
            }
    }

    @Test
    fun `access protected endpoint with token`() {
        val loginRequest = LoginRequest(
            username = "newuser",
            password = "password123"
        )

        val result = mockMvc.perform(
            MockMvcRequestBuilders.post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest))
        )
            .andExpect(status().isOk)
            .andReturn()

        val tokenResponse = objectMapper.readValue(result.response.contentAsString, Map::class.java)
        val token = tokenResponse["token"]

        mockMvc.perform(
            MockMvcRequestBuilders.get("/api/v1/applications")
                .header("Authorization", "Bearer $token")
        )
            .andExpect(status().isOk)
    }
}