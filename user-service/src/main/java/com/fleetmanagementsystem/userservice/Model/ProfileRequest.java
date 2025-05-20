package com.fleetmanagementsystem.userservice.Model;

import com.fleetmanagementsystem.userservice.organization.Organization;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
public class ProfileRequest {
    private String id;
    private MenuWithPermissions menu;
    private String name;
    private String role;
    private String organization;
    private String description;
}
