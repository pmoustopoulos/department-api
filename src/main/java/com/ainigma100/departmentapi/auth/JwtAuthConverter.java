package com.ainigma100.departmentapi.auth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimNames;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class JwtAuthConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    private final JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter =
            new JwtGrantedAuthoritiesConverter();

    @Value("${keycloak.client-id}")
    private String clientId;

    @Value("${keycloak.principle-attribute}")
    private String principleAttribute;

    @Override
    public AbstractAuthenticationToken convert(@NonNull Jwt jwt) {

        Collection<GrantedAuthority> grantedAuthorities = Stream.concat(
                jwtGrantedAuthoritiesConverter.convert(jwt).stream(),
                extractResourceRoles(jwt).stream()
        ).collect(Collectors.toSet());

        String principleClaimName = principleAttribute != null ? jwt.getClaimAsString(principleAttribute) : jwt.getClaimAsString(JwtClaimNames.SUB);

        return new JwtAuthenticationToken(jwt, grantedAuthorities, principleClaimName);
    }



    private Collection<? extends GrantedAuthority> extractResourceRoles(Jwt jwt) {

        Map<String, Object> resourceAccess;
        Map<String, Object> resource;
        Collection<String> resourceRoles;

        if (jwt.getClaim("resource_access") == null) {
            // return empty collection
            return Set.of();
        }

        resourceAccess = jwt.getClaim("resource_access");

        if (resourceAccess.get(clientId) == null) {
            // return empty collection
            return Set.of();
        }

        resource = (Map<String, Object>) resourceAccess.get(clientId);

        resourceRoles = (Collection<String>) resource.get("roles");

        return resourceRoles
                .stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(Collectors.toSet());

    }


}
