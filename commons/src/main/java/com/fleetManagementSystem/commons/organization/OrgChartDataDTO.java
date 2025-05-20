package com.fleetManagementSystem.commons.organization;

import lombok.Data;

import java.util.List;

@Data
public class OrgChartDataDTO {
    private String id;
    private String name;
    private String parentId;

    private List<String> users;
    private List<String> vehicles;
    private List<String> drivers;

    private List<OrgChartDataDTO> children; // Children for the tree structure
}