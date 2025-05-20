    package com.fleetmanagementsystem.userservice.keycloak;


    import org.keycloak.OAuth2Constants;
    import org.keycloak.admin.client.CreatedResponseUtil;
    import org.keycloak.admin.client.Keycloak;
    import org.keycloak.admin.client.KeycloakBuilder;
    import org.keycloak.admin.client.resource.RealmResource;
    import org.keycloak.admin.client.resource.UserResource;
    import org.keycloak.admin.client.resource.UsersResource;
    import org.keycloak.representations.idm.ClientRepresentation;
    import org.keycloak.representations.idm.CredentialRepresentation;
    import org.keycloak.representations.idm.RoleRepresentation;
    import org.keycloak.representations.idm.UserRepresentation;

    import javax.ws.rs.core.Response;
    import java.util.Arrays;
    import java.util.Collections;

    public class KeycloakAdminClientExample {

        public static void main(String[] args) {

            String serverUrl = "http://localhost:9090";
            String realm = "fleet-management-system";
            // idm-client needs to allow "Direct Access Grants: Resource Owner Password Credentials Grant"
            String clientId = "admin-cli";
            String clientSecret = "aamOE7QzooCJhapsGPOLcCuvGI428sBt";

    //		// Client "idm-client" needs service-account with at least "manage-users, view-clients, view-realm, view-users" roles for "realm-management"
    		Keycloak keycloak = KeycloakBuilder.builder()
    				.serverUrl(serverUrl)
    				.realm(realm)
    				.grantType(OAuth2Constants.CLIENT_CREDENTIALS)
    				.clientId(clientId)
    				.clientSecret(clientSecret).build();

            // User "idm-admin" needs at least "manage-users, view-clients, view-realm, view-users" roles for "realm-management"
//            Keycloak keycloak = KeycloakBuilder.builder() //
//                    .serverUrl(serverUrl) //
//                    .realm(realm) //
//                    .grantType(OAuth2Constants.PASSWORD) //
//                    .clientId(clientId) //
//                    .clientSecret(clientSecret) //
//                    .username("admin") //
//                    .password("admin") //
//                    .build();

            // Define user
            UserRepresentation user = new UserRepresentation();
            user.setEnabled(true);
            user.setUsername("tester1");
            user.setFirstName("First");
            user.setLastName("Last");
            user.setEmail("tom+tester1@tdlabs.local");

            user.setAttributes(Collections.singletonMap("origin", Arrays.asList("demo")));
            user.setAttributes(Collections.singletonMap("organization", Arrays.asList("example-org")));

            // Get realm
            RealmResource realmResource = keycloak.realm(realm);
            UsersResource usersRessource = realmResource.users();

            // Create user (requires manage-users role)
            Response response = usersRessource.create(user);
            System.out.printf("Response: %s %s%n", response.getStatus(), response.getStatusInfo());
            System.out.println("Response Body: " + response.readEntity(String.class));


        }
    }