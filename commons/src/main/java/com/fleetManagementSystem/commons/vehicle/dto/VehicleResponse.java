package com.fleetManagementSystem.commons.vehicle.dto;

import com.fleetManagementSystem.commons.driver.model.Driver;
import com.fleetManagementSystem.commons.organization.Organization;
import com.fleetManagementSystem.commons.position.dto.PositionResponse;
import com.fleetManagementSystem.commons.position.dto.PositionVlResponse;
import com.fleetManagementSystem.commons.tracker.dto.MinimalTrackerResponse;
import com.fleetManagementSystem.commons.vehicle.model.RuleGroup;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class VehicleResponse {


    private String id;

    private String licensePlate;

    private String modelVehicle;

    private String brandVehicle;

    private int year;

    private String color;

    private String fuelType;

    private String status;

    private Boolean isMoving;

    private String vehicleType;

    private Organization organization;

    private String imgPath;



    @Builder.Default
    private Driver currentDriver = null;
    @Builder.Default
    private PositionVlResponse lastPosition = null;

    private MinimalTrackerResponse tracker;

    private List<RuleGroup> ruleGroupList;


}
