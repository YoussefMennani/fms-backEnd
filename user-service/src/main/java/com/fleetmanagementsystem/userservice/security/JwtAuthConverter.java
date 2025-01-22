package com.fleetmanagementsystem.userservice.security;


import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
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

@RequiredArgsConstructor
@Component
public class JwtAuthConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    private static final JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();

    private final JwtAuthConverterProperties properties;


    private Collection<? extends GrantedAuthority> extractRealmRoles(Jwt jwt) {
        Map<String, Object> realmAccess = jwt.getClaim("realm_access");
        Collection<String> realmRoles;
        if (realmAccess == null || (realmRoles = (Collection<String>) realmAccess.get("roles")) == null) {
            return Set.of();
        }
        return realmRoles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(Collectors.toSet());
    }

    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
        Collection<GrantedAuthority> authorities =
                Stream.concat(
                        jwtGrantedAuthoritiesConverter.convert(jwt).stream(),
                        Stream.concat(
                                extractResourceRoles(jwt).stream(),
                                extractRealmRoles(jwt).stream()
                        )
                ).collect(Collectors.toSet());

        String claimName = properties.getPrincipalAttribute() == null ? JwtClaimNames.SUB : properties.getPrincipalAttribute();
        return new JwtAuthenticationToken(jwt, authorities, jwt.getClaim(claimName));
    }


//    @Override
//    public AbstractAuthenticationToken convert(Jwt jwt) {
//        Collection<GrantedAuthority> authorities =
//                Stream.concat(jwtGrantedAuthoritiesConverter.convert(jwt).stream(), extractResourceRoles(jwt).stream()).collect(Collectors.toSet());
//        String claimName = properties.getPrincipalAttribute() == null ? JwtClaimNames.SUB : properties.getPrincipalAttribute();
//        return new JwtAuthenticationToken(jwt, authorities, jwt.getClaim(claimName));
//    }
//
    private Collection<? extends GrantedAuthority> extractResourceRoles(Jwt jwt) {
        Map<String, Object> resourceAccess = jwt.getClaim("resource_access");
        Map<String, Object> resource;
        Collection<String> resourceRoles;
        if (resourceAccess == null
                || (resource = (Map<String, Object>) resourceAccess.get(properties.getResourceId())) == null
                || (resourceRoles = (Collection<String>) resource.get("roles")) == null) {
            return Set.of();
        }
        return resourceRoles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(Collectors.toSet());
    }
}
