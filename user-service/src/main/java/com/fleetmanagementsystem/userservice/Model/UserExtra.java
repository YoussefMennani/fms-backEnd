package com.fleetmanagementsystem.userservice.Model;
import com.fleetmanagementsystem.userservice.organization.Organization;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Map;

@Data
@Document(collection = "userextras")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserExtra {

    @Id
    private String id;
    private String keycloakId; // Keycloak user ID
    private String username;
    private String firstName;
    private String lastName;
    private String email;

    private String avatar;
    @DBRef(lazy = true)
    private Profile profile;
    @DBRef(lazy = true)
    private Organization organization;
    private boolean authorizedUser = true;
    public UserExtra(String username) {
        this.username = username;
    }
}
