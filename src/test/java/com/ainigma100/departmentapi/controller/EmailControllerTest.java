package com.ainigma100.departmentapi.controller;

import com.ainigma100.departmentapi.enums.Status;
import com.ainigma100.departmentapi.service.EmailService;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.concurrent.CompletableFuture;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/*
 * @WebMvcTest annotation will load all the components required
 * to test the Controller layer. It will not load the service or repository layer components
 */
@WebMvcTest(EmailController.class)
@Tag("unit")
class EmailControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmailService emailService;


    @Test
    void givenNoInput_whenSendEmailWithoutAttachment_thenReturnTrueIfMailWasSent() throws Exception {

        // given - precondition or setup
        given(emailService.sendEmailWithoutAttachment()).willReturn(CompletableFuture.completedFuture(true));

        final MvcResult mvcResult = mockMvc.perform(get("/api/v1/emails"))
                .andExpect(request().asyncStarted()) // Expect the request to be started asynchronously
                .andReturn();

        // when - action or behaviour that we are going to test
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
    void givenNoInput_whenSendEmailWithAttachment_thenReturnTrueIfMailWasSent() throws Exception {

        // given - precondition or setup
        given(emailService.sendEmailWithAttachment()).willReturn(CompletableFuture.completedFuture(true));

        final MvcResult mvcResult = mockMvc.perform(get("/api/v1/emails/with-attachment"))
                .andExpect(request().asyncStarted()) // Expect the request to be started asynchronously
                .andReturn();

        // when - action or behaviour that we are going to test
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

}
