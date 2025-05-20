package com.fleetmanagementsystem.listnerservice.FeignClient;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.json.JSONObject;

@Service
public class JwtTokenService {

    private String tokenUri = "http://localhost:9090/realms/fleet-management-system/protocol/openid-connect/token";
    private String clientId = "listener-client"; // Update to client-listener
    private String clientSecret = "NIqu6THXwIVhiIRhUQjlblDUkXRVYzUu"; // Use the correct secret for client-listener

    private final WebClient webClient;

    public JwtTokenService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl(tokenUri).build();
    }

    public String getJwtToken() {
        String body = "client_id=" + clientId +
                "&client_secret=" + clientSecret +
                "&grant_type=client_credentials";

        try {
            // Request token from Keycloak and extract the access token
            String response = webClient.post()
                    .uri("")
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .bodyValue(body)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            return extractJwtToken(response);
        } catch (WebClientResponseException e) {
            // Log and handle error
            System.out.println("Error response: " + e.getResponseBodyAsString());
            throw new RuntimeException("Failed to retrieve JWT token from Keycloak: " + e.getMessage(), e);
        }
    }

    private String extractJwtToken(String response) {
        try {
            JSONObject json = new JSONObject(response);
            return json.getString("access_token");
        } catch (Exception e) {
            throw new RuntimeException("Failed to extract JWT token", e);
        }
    }
}

