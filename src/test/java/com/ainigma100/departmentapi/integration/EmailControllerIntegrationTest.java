package com.ainigma100.departmentapi.integration;

import com.ainigma100.departmentapi.enums.Status;
import com.ainigma100.departmentapi.filter.RateLimitingFilter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// Use random port for integration testing. the server will start on a random port
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Testcontainers(disabledWithoutDocker = true)
@ActiveProfiles("testcontainers")
@Tag("integration")
class EmailControllerIntegrationTest extends AbstractContainerBaseTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RateLimitingFilter rateLimitingFilter;


    @AfterEach
    void resetRateLimitBuckets() {
        rateLimitingFilter.clearBuckets();
    }

    @Test
    void givenNoInput_whenSendEmailWithoutAttachment_thenReturnTrue() throws Exception {

        // given - precondition or setup

        // when - action or behaviour that we are going to test
        ResultActions response = mockMvc.perform(get("/api/v1/emails"));

        // then - verify the output
        response.andDo(print())
                // verify the status code that is returned
                .andExpect(status().isOk())
                // verify the actual returned value and the expected value
                // $ - root member of a JSON structure whether it is an object or array
                .andExpect(jsonPath("$.status", is(Status.SUCCESS.getValue())));

    }

    @Test
    void givenNoInput_whenSendEmailWithAttachment_thenReturnTrue() throws Exception {

        // given - precondition or setup

        // when - action or behaviour that we are going to test
        ResultActions response = mockMvc.perform(get("/api/v1/emails/with-attachment"));

        // then - verify the output
        response.andDo( print() )
                // verify the status code that is returned
                .andExpect( status().isOk() )
                // verify the actual returned value and the expected value
                // $ - root member of a JSON structure whether it is an object or array
                .andExpect(jsonPath("$.status", is(Status.SUCCESS.getValue())));

    }
}
