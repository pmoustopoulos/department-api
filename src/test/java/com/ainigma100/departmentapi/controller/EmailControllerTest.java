package com.ainigma100.departmentapi.controller;

import com.ainigma100.departmentapi.service.EmailService;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
        given(emailService.sendEmailWithoutAttachment()).willReturn(true);

        // when - action or behaviour that we are going to test
        ResultActions response = mockMvc.perform(get("/api/v1/emails"));

        // then - verify the output
        response.andDo(print())
                // verify the status code that is returned
                .andExpect(status().isOk());

    }


    @Test
    void givenNoInput_whenSendEmailWithAttachment_thenReturnTrueIfMailWasSent() throws Exception {

        // given - precondition or setup
        given(emailService.sendEmailWithAttachment()).willReturn(true);

        // when - action or behaviour that we are going to test
        ResultActions response = mockMvc.perform(get("/api/v1/emails/with-attachment"));

        // then - verify the output
        response.andDo(print())
                // verify the status code that is returned
                .andExpect(status().isOk());

    }

}