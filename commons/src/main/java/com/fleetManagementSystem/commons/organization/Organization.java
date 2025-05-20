package com.fleetManagementSystem.commons.organization;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "organizations_collection")
@AllArgsConstructor
@NoArgsConstructor
public class Organization {
    @Id
    private String id;
    private String name;
    private String parentId; // Reference to the parent organization
}