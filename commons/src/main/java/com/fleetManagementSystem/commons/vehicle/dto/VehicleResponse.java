package com.fleetManagementSystem.commons.vehicle.dto;

import com.fleetManagementSystem.commons.driver.model.Driver;
import com.fleetManagementSystem.commons.position.dto.PositionResponse;
import com.fleetManagementSystem.commons.tracker.dto.MinimalTrackerResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    private String organization;

    private String imgPath;

    @Builder.Default
    private Driver currentDriver = null;
    @Builder.Default
    private PositionResponse lastPosition = null;

    private MinimalTrackerResponse tracker;

}
