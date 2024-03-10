package com.ainigma100.departmentapi.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {

    @Value("${springdoc.version}")
    private String springDocVersion;

    @Value("${spring.application.name}")
    private String applicationName;

    @Value("${keycloak.enabled:false}")
    private boolean keycloakEnabled;


    @Bean
    @ConditionalOnProperty(name = "keycloak.enabled", havingValue = "true", matchIfMissing = true)
    public OpenAPI customizeOpenAPIWithSecurity() {
        final String securitySchemeName = "bearerAuth";

        return new OpenAPI()
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName, new SecurityScheme()
                                .name(securitySchemeName)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")))
                .info(new Info()
                        .title("Department API")
                        .version(springDocVersion)
                        .description("Documentation " + applicationName + " v1.0"));
    }

    @Bean
    @ConditionalOnProperty(name = "keycloak.enabled", havingValue = "false")
    public OpenAPI customizeOpenAPIWithoutSecurity() {
        return new OpenAPI()
                .info(new Info()
                        .title("Department API")
                        .version(springDocVersion)
                        .description("**Security is disabled.**"));
    }
}
