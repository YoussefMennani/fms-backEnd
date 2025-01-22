package com.fleetmanagementsystem.userservice.service;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;

import javax.ws.rs.core.Response;
import java.util.Collections;

@Service
public class KeycloakService______old {
    private final Keycloak keycloak;
    private final String realm;

    public KeycloakService______old() {
        this.keycloak = Keycloak.getInstance(
                "http://localhost:9090/auth",   // Keycloak base URL
                "fleet-management-system",                      // Realm
                "admin",                       // Admin username
                "admin",              // Admin password
                "fms"           // Client ID
        );
        this.realm = "fleet-management-system";    // Your realm name
    }

    public String createUser(String username, String email, String password) {
        UsersResource usersResource = keycloak.realm(realm).users();

        UserRepresentation user = new UserRepresentation();
        user.setUsername(username);
        user.setEmail(email);
        user.setEnabled(true);
        user.setAttributes(Collections.singletonMap("customAttribute", Collections.singletonList("value")));

        Response response = usersResource.create(user);

        if (response.getStatus() != 201) {
            throw new RuntimeException("Failed to create user in Keycloak");
        }

        String locationHeader = response.getHeaderString("Location");
        return locationHeader.substring(locationHeader.lastIndexOf("/") + 1); // Extract Keycloak user ID
    }
}
