package com.ainigma100.departmentapi.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class KeycloakTokenRequestDTO {

    @NotEmpty(message = "username should not be null or empty")
    @Schema(example = "petros")
    private String username;

    @NotEmpty(message = "password should not be null or empty")
    @Schema(example = "pass")
    private String password;

}
