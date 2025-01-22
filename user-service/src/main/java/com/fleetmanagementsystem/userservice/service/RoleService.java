package com.fleetmanagementsystem.userservice.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Collection;
import java.util.List;

@Service
public class RoleService {

    public  boolean isAuthorized(Principal principal, List<String> listRoles){
        Authentication authentication = (Authentication) principal;
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();



        // Check if the role exists in the authorities
        boolean roleExists = authorities.stream()
                .anyMatch(authority -> listRoles.contains(authority.getAuthority()) );

        if (roleExists) {
            System.out.println("Role " + listRoles + " exists.");
        } else {
            System.out.println("Role " + listRoles + " does not exist.");
        }
        return roleExists;
    }

    public String extractIdOrganization(Principal principal) {
        if (!(principal instanceof JwtAuthenticationToken)) {
            throw new IllegalArgumentException("Principal is not a JwtAuthenticationToken");
        }

        JwtAuthenticationToken jwtAuthenticationToken = (JwtAuthenticationToken) principal;
        Jwt jwt = (Jwt) jwtAuthenticationToken.getPrincipal();

        // Safely retrieve the "organization" claim
        String organization = jwt.getClaimAsString("organization");

        if (organization == null) {
            throw new IllegalArgumentException("Organization claim not found in JWT");
        }

        return organization;
    }

}
