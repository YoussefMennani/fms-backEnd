package com.fleetManagementSystem.commons.vehicle.model;


import com.fleetManagementSystem.commons.driver.model.Driver;
import com.fleetManagementSystem.commons.organization.Organization;
import com.fleetManagementSystem.commons.position.dto.PositionResponse;
import com.fleetManagementSystem.commons.position.dto.PositionVlResponse;
import com.fleetManagementSystem.commons.tracker.dto.MinimalTrackerResponse;
import com.fleetManagementSystem.commons.vehicle.dto.VehicleRequest;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Document("vehicles")
public class Vehicle {

    @Id
    private String id;

    private String licensePlate;

    private String modelVehicle;

    private String brandVehicle;

    private int year;

    private String color;

    private String fuelType;

    private String vehicleType;

    private String status;

    @DBRef(lazy = true)
    private Driver currentDriver;

    private PositionVlResponse lastPosition;

    @DBRef(lazy = true)
    private Organization organization;

    private MinimalTrackerResponse tracker;

//    @Builder.Default
//    private List<RuleGroup> ruleGroupList = new ArrayList<>();
    @DBRef(lazy = true)
    private List<RuleGroup> ruleGroupList = new ArrayList<>();

    @Builder.Default
    private boolean isMoving = false;

    @Builder.Default
    private String imgPath = "image-vehicle/1734644895869";

    @CreatedDate
    private Date createdAt;

    @LastModifiedDate
    private Date updatedAt;

    public boolean isEquivalentTo(VehicleRequest request) {
        if (request == null) {
            return false;
        }

        // Check each field for equality
//        boolean idEquals = (id == null && request.id() == null) || (id != null && id.equals(request.id()));
        boolean licensePlateEquals = licensePlate.equals(request.getLicensePlate());
        boolean modelVehicleEquals = modelVehicle.equals(request.getModelVehicle());
        boolean brandVehicleEquals = brandVehicle.equals(request.getBrandVehicle());
        boolean yearEquals = year == request.getYear(); // Direct list comparison
        boolean colorEquals = color.equals(request.getColor()); // Assuming `TrackerBrand` has a `getName()` method
        boolean fuelTypeEquals = fuelType.equals(request.getFuelType());
        boolean organizationEquals = organization.equals(request.getOrganization());
        boolean statusEquals = status.equals(request.getStatus());
        boolean vehicleTypeEquals = vehicleType.equals(request.getVehicleType());

        boolean trackerEquals = tracker.getTrackerId().equals(request.getTracker().getTrackerId());

        // Return true only if all fields match
        return vehicleTypeEquals && licensePlateEquals && modelVehicleEquals && brandVehicleEquals && yearEquals && colorEquals && fuelTypeEquals && statusEquals && trackerEquals && organizationEquals;
    }

    // Getters and Setters


}
