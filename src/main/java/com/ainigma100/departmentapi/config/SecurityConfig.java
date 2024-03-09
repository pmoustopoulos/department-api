package com.ainigma100.departmentapi.config;

import com.ainigma100.departmentapi.auth.JwtAuthConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthConverter jwtAuthConverter;


    private static final String[] PUBLIC_URL = {
            "/api/auth/**", "/ui/**",
            "/swagger-ui-custom.html", "/swagger-ui.html",
            "/swagger-ui/**", "/v3/api-docs/**", "/webjars/**",
            "/swagger-ui/index.html", "/api-docs/**",
            "/api/v1/generate-token"
    };


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {


        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers("/api/v1/departments/**", "/api/v1/employees/**", "/api/v1/emails/**", "/api/v1/reports/**").authenticated()
                        .requestMatchers(PUBLIC_URL).permitAll())
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                // inform spring to validate the jwt using oauth2 resource server
                .oauth2ResourceServer(oauth2 ->
                        oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthConverter))
                )
                .sessionManagement(httpSecuritySessionManagementConfigurer ->
                        httpSecuritySessionManagementConfigurer.sessionCreationPolicy(STATELESS)
                );


        return http.build();

    }

}
