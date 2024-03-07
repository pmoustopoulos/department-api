package com.ainigma100.departmentapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
public class SecurityConfig {


    private static final String[] PUBLIC_URL = {
            "/api/auth/**", "/ui/**",
            "/swagger-ui-custom.html", "/swagger-ui.html",
            "/swagger-ui/**", "/v3/api-docs/**", "/webjars/**",
            "/swagger-ui/index.html", "/api-docs/**"
    };


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.csrf(AbstractHttpConfigurer::disable);

        http.authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/api/v1/departments/**", "/api/v1/employees/**", "/api/v1/emails/**", "/api/v1/reports/**").authenticated()
                        .requestMatchers(PUBLIC_URL).permitAll())
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable);


        // inform spring to validate the jwt using oauth2 resource server
        http.oauth2ResourceServer((oauth2) -> oauth2.jwt(Customizer.withDefaults()));

        http.sessionManagement(httpSecuritySessionManagementConfigurer ->
                httpSecuritySessionManagementConfigurer.sessionCreationPolicy(STATELESS)
        );

        return http.build();

    }

}
