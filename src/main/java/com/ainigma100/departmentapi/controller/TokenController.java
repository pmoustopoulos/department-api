package com.ainigma100.departmentapi.controller;

import com.ainigma100.departmentapi.dto.APIResponse;
import com.ainigma100.departmentapi.dto.DepartmentDTO;
import com.ainigma100.departmentapi.dto.KeycloakTokenRequestDTO;
import com.ainigma100.departmentapi.dto.KeycloakTokenResponseDTO;
import com.ainigma100.departmentapi.enums.Status;
import com.ainigma100.departmentapi.service.TokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "1. Generate token")
@AllArgsConstructor
@RequestMapping("/api/v1")
@RestController
// Include this controller only when security is activated
@ConditionalOnProperty(name = "keycloak.enabled", havingValue = "true", matchIfMissing = true)
public class TokenController {


    private final TokenService tokenService;


    @SecurityRequirements // removes the lock symbol
    @Operation(summary = "Generate token for accessing protected resources",
            description = "Generate a token for accessing protected resources. Use username 'petros' and password 'pass' " +
                    "for 'client_admin' role, and username 'user' and password 'pass' for 'client_user' role.")
    @PostMapping("/generate-token")
    public ResponseEntity<APIResponse<KeycloakTokenResponseDTO>> generateToken(
            @Valid @RequestBody KeycloakTokenRequestDTO keycloakTokenRequestDTO) {

        KeycloakTokenResponseDTO result = tokenService.generateToken(keycloakTokenRequestDTO);


        // Builder Design pattern
        APIResponse<KeycloakTokenResponseDTO> responseDTO = APIResponse
                .<KeycloakTokenResponseDTO>builder()
                .status(Status.SUCCESS.getValue())
                .results(result)
                .build();


        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

}
