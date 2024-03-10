package com.ainigma100.departmentapi.service.impl;

import com.ainigma100.departmentapi.dto.KeycloakTokenRequestDTO;
import com.ainigma100.departmentapi.dto.KeycloakTokenResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

/*
 * @ExtendWith(MockitoExtension.class) informs Mockito that we are using
 * mockito annotations to mock the dependencies
 */
@ExtendWith(MockitoExtension.class)
@Tag("unit")
class TokenServiceImplTest {

    // @InjectMocks creates the mock object of the class and injects the mocks
    // that are marked with the annotations @Mock into it.
    @InjectMocks
    private TokenServiceImpl tokenService;

    @Mock
    private RestTemplate restTemplate;


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

        // The following reflection is used to mock the @Value property
        // inside the TokenServiceImpl class
        ReflectionTestUtils.setField(tokenService, "tokenEndpoint", "http://localhost:9090/realms/ainigma100/protocol/openid-connect/token");
        ReflectionTestUtils.setField(tokenService, "clientId", "department-api");

    }


    // TODO: Implement this unit test
    @Test
    void given_when_then() {

        // given - precondition or setup


        // when - action or behaviour that we are going to test


        // then - verify the output

    }


}
