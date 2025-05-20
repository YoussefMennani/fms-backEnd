package com.fleetmanagementsystem.userservice.keycloak;


import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KeycloakConfig {

    @Bean
    public Keycloak keycloak() {
        String serverUrl = "http://localhost:9090";
        String realm = "fleet-management-system";
        String clientId = "admin-cli";
        String clientSecret = "aamOE7QzooCJhapsGPOLcCuvGI428sBt";

        return KeycloakBuilder.builder()
                .serverUrl(serverUrl)
                .realm(realm)
                .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .build();
    }
}

