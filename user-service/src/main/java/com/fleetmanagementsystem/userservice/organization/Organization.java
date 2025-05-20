package com.fleetmanagementsystem.userservice.organization;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "organizations_collection")
@AllArgsConstructor
public class Organization {
    @Id
    private String id;
    private String name;
    private String parentId; // Reference to the parent organization
}