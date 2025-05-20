package com.fleetmanagementsystem.userservice.keycloak;

import com.fleetmanagementsystem.userservice.Model.Profile;
import com.fleetmanagementsystem.userservice.Model.UserExtra;
import com.fleetmanagementsystem.userservice.organization.Organization;
import com.fleetmanagementsystem.userservice.repository.ProfilRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

import javax.ws.rs.NotFoundException;
import java.security.Principal;
import java.util.Map;

@Component
public class UserExtraMapper {

    @Autowired
    private ProfilRepository profilRepository;


    public UserExtra mapJwtClaimsToUserExtra(Principal principal) {
        if (!(principal instanceof JwtAuthenticationToken)) {
            throw new IllegalArgumentException("Principal is not a JwtAuthenticationToken");
        }

        Profile profile = this.profilRepository.findById("6790290a92b5742d5d54fe83").orElseThrow(()->new NotFoundException("Profile super admin not found"));

        JwtAuthenticationToken jwtAuthenticationToken = (JwtAuthenticationToken) principal;
        Jwt jwt = (Jwt) jwtAuthenticationToken.getCredentials(); // Use getCredentials() to access the JWT
        Map<String, Object> claims = jwt.getClaims();

        // Extract claims and map them to UserExtra fields
        UserExtra userExtra = UserExtra.builder()
                .keycloakId((String) claims.get("sub")) // Assuming "sub" is the Keycloak user ID
                .username((String) claims.get("preferred_username")) // Assuming "preferred_username" is the username
                .firstName((String) claims.get("given_name")) // Assuming "given_name" is the first name
                .lastName((String) claims.get("family_name")) // Assuming "family_name" is the last name
                .email((String) claims.get("email")) // Assuming "email" is the email
                .avatar((String) claims.get("avatar")) // Assuming "avatar" is a custom claim
                .organization(null)
                .profile(profile)
                .build();


        return userExtra;
    }
}