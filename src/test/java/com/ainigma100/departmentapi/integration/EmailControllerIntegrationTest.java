package com.ainigma100.departmentapi.integration;

import com.ainigma100.departmentapi.enums.Status;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// Use random port for integration testing. the server will start on a random port
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Testcontainers(disabledWithoutDocker = true)
@Tag("integration")
class EmailControllerIntegrationTest extends AbstractContainerBaseTest {

    @Autowired
    private MockMvc mockMvc;


    @Test
    void givenNoInput_whenSendEmailWithoutAttachment_thenReturnTrue() throws Exception {

        // given - precondition or setup

        // when - action or behaviour that we are going to test
        final MvcResult mvcResult = mockMvc.perform(get("/api/v1/emails"))
                .andExpect(MockMvcResultMatchers.request().asyncStarted()) // Expect the request to be started asynchronously
                .andReturn();

        // Dispatch the async request and get the result
        ResultActions response = mockMvc.perform(asyncDispatch(mvcResult));

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
        final MvcResult mvcResult = mockMvc.perform(get("/api/v1/emails/with-attachment"))
                .andExpect(MockMvcResultMatchers.request().asyncStarted()) // Expect the request to be started asynchronously
                .andReturn();

        // Dispatch the async request and get the result
        ResultActions response = mockMvc.perform(asyncDispatch(mvcResult));

        // then - verify the output
        response.andDo( print() )
                // verify the status code that is returned
                .andExpect( status().isOk() )
                // verify the actual returned value and the expected value
                // $ - root member of a JSON structure whether it is an object or array
                .andExpect(jsonPath("$.status", is(Status.SUCCESS.getValue())));

    }
}
