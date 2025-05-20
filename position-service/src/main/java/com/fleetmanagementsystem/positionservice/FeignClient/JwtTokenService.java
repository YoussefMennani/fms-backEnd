package com.fleetmanagementsystem.positionservice.FeignClient;

import jakarta.annotation.PostConstruct;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.time.Instant;

@Service
public class JwtTokenService {

    private final WebClient webClient;

    @Value("${keycloak.token-uri}")
    private String tokenUri;

    @Value("${keycloak.client-id}")
    private String clientId;

    @Value("${keycloak.client-secret}")
    private String clientSecret;

    private String cachedToken;
    private Instant expiryTime;

    public JwtTokenService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    public String getJwtToken() {
        if (cachedToken != null && Instant.now().isBefore(expiryTime)) {
            return cachedToken;
        }

        String body = "client_id=" + clientId +
                "&client_secret=" + clientSecret +
                "&grant_type=client_credentials";

        try {
            String response = webClient.post()
                    .uri(tokenUri)
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .bodyValue(body)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            JSONObject json = new JSONObject(response);
            cachedToken = json.getString("access_token");
            int expiresIn = json.getInt("expires_in");
            expiryTime = Instant.now().plusSeconds(expiresIn - 60); // refresh 1 minute before expiry

            return cachedToken;
        } catch (WebClientResponseException e) {
            System.err.println("Keycloak token error: " + e.getResponseBodyAsString());
            throw new RuntimeException("Failed to retrieve JWT token from Keycloak", e);
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error while fetching JWT token", e);
        }
    }
}
