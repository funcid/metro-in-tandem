package me.func.internal

import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension

@DirtiesContext
@ActiveProfiles("test")
@ExtendWith(SpringExtension::class)
@SpringBootTest(
    classes = [InternalApplication::class],
    webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,
)
@AutoConfigureMockMvc
abstract class AbstractTest