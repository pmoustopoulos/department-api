package com.ainigma100.departmentapi.service.impl;

import com.ainigma100.departmentapi.dto.KeycloakTokenRequestDTO;
import com.ainigma100.departmentapi.dto.KeycloakTokenResponseDTO;
import com.ainigma100.departmentapi.service.TokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

@Slf4j
@Service
public class TokenServiceImpl implements TokenService {


    @Value("${spring.security.oauth2.resourceserver.jwt.token-endpoint}")
    private String tokenEndpoint;

    @Value("${keycloak.client-id}")
    private String clientId;



    @Override
    public KeycloakTokenResponseDTO generateToken(KeycloakTokenRequestDTO keycloakTokenRequestDTO) {

        RestClient restClient = RestClient
                .builder()
                .build();


        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "password");
        body.add("client_id", clientId);
        body.add("username", keycloakTokenRequestDTO.getUsername());
        body.add("password", keycloakTokenRequestDTO.getPassword());


        return restClient
                .post()
                .uri(tokenEndpoint)
                .accept(MediaType.APPLICATION_FORM_URLENCODED)
                .body(body)
                .retrieve()
                .toEntity(KeycloakTokenResponseDTO.class).getBody();
    }


}
