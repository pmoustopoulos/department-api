package com.ainigma100.departmentapi.service;

import com.ainigma100.departmentapi.dto.KeycloakTokenRequestDTO;
import com.ainigma100.departmentapi.dto.KeycloakTokenResponseDTO;

public interface TokenService {

    KeycloakTokenResponseDTO generateToken(KeycloakTokenRequestDTO keycloakTokenRequestDTO);

}
