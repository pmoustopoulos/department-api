package com.ainigma100.departmentapi.controller;

import com.ainigma100.departmentapi.dto.KeycloakTokenRequestDTO;
import com.ainigma100.departmentapi.dto.KeycloakTokenResponseDTO;
import com.ainigma100.departmentapi.enums.Status;
import com.ainigma100.departmentapi.service.TokenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/*
 * @WebMvcTest annotation will load all the components required
 * to test the Controller layer. It will not load the service or repository layer components
 */
@WebMvcTest(TokenController.class)
@Tag("unit")
@AutoConfigureMockMvc(addFilters = false) // Disable Spring Security filters
class TokenControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TokenService tokenService;


    private KeycloakTokenRequestDTO keycloakTokenRequestDTO;
    private KeycloakTokenResponseDTO keycloakTokenResponseDTO;

    @BeforeEach
    void setUp() {

        keycloakTokenRequestDTO = new KeycloakTokenRequestDTO();
        keycloakTokenRequestDTO.setUsername("petros");
        keycloakTokenRequestDTO.setPassword("pass");

        keycloakTokenResponseDTO = new KeycloakTokenResponseDTO();
        keycloakTokenResponseDTO.setAccessToken("mockAccessToken");
        keycloakTokenResponseDTO.setExpiresIn(1200);
        keycloakTokenResponseDTO.setRefreshExpiresIn(1800);
        keycloakTokenResponseDTO.setRefreshToken("mockRefreshToken");
        keycloakTokenResponseDTO.setTokenType("Bearer");
        keycloakTokenResponseDTO.setNotBeforePolicy(0);
        keycloakTokenResponseDTO.setSessionState("mockSessionState");
        keycloakTokenResponseDTO.setScope("email profile");

    }


    @Test
    void givenKeycloakTokenRequestDTO_whenGenerateToken_thenReturnKeycloakTokenResponseDTO() throws Exception {

        // given - precondition or setup
        given(tokenService.generateToken(any(KeycloakTokenRequestDTO.class)))
                .willReturn(keycloakTokenResponseDTO);

        // when - action or behaviour that we are going to test
        ResultActions response = mockMvc.perform(post("/api/v1/generate-token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(keycloakTokenRequestDTO)));

        // then - verify the output
        response.andDo(print())
                // verify the status code that is returned
                .andExpect(status().isOk())
                // verify the actual returned value and the expected value
                // $ - root member of a JSON structure whether it is an object or array
                .andExpect(jsonPath("$.status", is(Status.SUCCESS.getValue())))
                .andExpect(jsonPath("$.results.access_token").value(keycloakTokenResponseDTO.getAccessToken()))
                .andExpect(jsonPath("$.results.expires_in").value(keycloakTokenResponseDTO.getExpiresIn()))
                .andExpect(jsonPath("$.results.refresh_expires_in").value(keycloakTokenResponseDTO.getRefreshExpiresIn()))
                .andExpect(jsonPath("$.results.refresh_token").value(keycloakTokenResponseDTO.getRefreshToken()))
                .andExpect(jsonPath("$.results.token_type").value(keycloakTokenResponseDTO.getTokenType()))
                .andExpect(jsonPath("$.results.not-before-policy").value(keycloakTokenResponseDTO.getNotBeforePolicy()))
                .andExpect(jsonPath("$.results.session_state").value(keycloakTokenResponseDTO.getSessionState()))
                .andExpect(jsonPath("$.results.scope").value(keycloakTokenResponseDTO.getScope()));


    }

}
