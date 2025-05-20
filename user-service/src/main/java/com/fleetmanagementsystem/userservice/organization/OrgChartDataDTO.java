package com.fleetmanagementsystem.userservice.organization;

import com.fleetManagementSystem.commons.driver.model.DriverResponse;
import com.fleetManagementSystem.commons.vehicle.dto.VehicleResponse;
import com.fleetmanagementsystem.userservice.Model.UserExtra;
import lombok.Data;

import java.util.List;

@Data
public class OrgChartDataDTO {
    private String id;
    private String name;
    private String parentId;

    private List<UserExtra> users;
    private List<VehicleResponse> vehicles;
    private List<DriverResponse> drivers;
    private boolean visible = true;
    private List<OrgChartDataDTO> children; // Children for the tree structure
}