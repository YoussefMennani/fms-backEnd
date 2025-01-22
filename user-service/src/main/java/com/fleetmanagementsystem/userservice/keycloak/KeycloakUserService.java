package com.fleetmanagementsystem.userservice.keycloak;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.ws.rs.core.Response;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class KeycloakUserService {

    @Autowired
    private Keycloak keycloak;

    private final String realm = "fleet-management-system";

    public String createUser(String username, String firstName, String lastName, String email, String password, String origin, String organization, List<String> roleNames) {
        // Define user
        UserRepresentation user = new UserRepresentation();
        user.setEnabled(true);
        user.setUsername(username);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);

        // Set custom attributes
        user.setAttributes(Collections.singletonMap("origin", Arrays.asList(origin)));
        user.setAttributes(Collections.singletonMap("organization", Arrays.asList(organization)));

        // Get realm
        RealmResource realmResource = keycloak.realm(realm);
        UsersResource usersResource = realmResource.users();

        // Create user (requires manage-users role)
        Response response = usersResource.create(user);
        System.out.printf("Response: %s %s%n", response.getStatus(), response.getStatusInfo());
        System.out.println("Response Body: " + response.readEntity(String.class));

        // Get the user ID from the response
        String userId = CreatedResponseUtil.getCreatedId(response);

        // Now, set the password for the user
        setUserPassword(userId, password);

        // Now, assign roles to the user
        assignRolesToUser(userId, roleNames);

        return userId;
    }

    private void setUserPassword(String userId, String password) {
        // Create a CredentialRepresentation object to set the password
        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(password); // Set the password

        // Get the user resource
        RealmResource realmResource = keycloak.realm(realm);
        UserResource userResource = realmResource.users().get(userId);

        // Set the password for the user
        userResource.resetPassword(credential);
        System.out.println("Password set for user: " + userId);
    }

    private void assignRolesToUser(String userId, List<String> roleNames) {
        // Get the roles resource for the realm
        RealmResource realmResource = keycloak.realm(realm);
        List<RoleRepresentation> roles = realmResource.roles().list(); // List all roles in the realm

        // Find the roles by name
        for (String roleName : roleNames) {
            RoleRepresentation role = realmResource.roles().get(roleName).toRepresentation();
            if (role != null) {
                // Get the user resource
                UserResource userResource = realmResource.users().get(userId);

                // Assign the role to the user
                userResource.roles().realmLevel().add(Arrays.asList(role));
                System.out.println("Assigned role: " + roleName);
            } else {
                System.out.println("Role not found: " + roleName);
            }
        }
    }
}
