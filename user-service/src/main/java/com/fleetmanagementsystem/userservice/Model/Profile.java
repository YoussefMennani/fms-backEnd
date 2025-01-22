package com.fleetmanagementsystem.userservice.Model;

import com.fleetmanagementsystem.userservice.organization.Organization;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "profiles") // Specify the MongoDB collection name
@Getter
@Setter
@Builder
public class Profile {
    @Id
    private String id;
    private MenuWithPermissions menu;
    private String name;
    private String role;
    @DBRef(lazy = true)
    private Organization organization;
    private String description;
}
