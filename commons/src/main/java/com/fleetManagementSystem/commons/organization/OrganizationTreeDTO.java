package com.fleetManagementSystem.commons.organization;

import lombok.Data;

import java.util.List;

@Data
public class OrganizationTreeDTO {
    private String id;
    private String name;
    private String parentId;
    private List<OrganizationTreeDTO> children; // Children for the tree structure
}