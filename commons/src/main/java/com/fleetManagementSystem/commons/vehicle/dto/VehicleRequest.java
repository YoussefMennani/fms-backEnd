package com.fleetManagementSystem.commons.vehicle.dto;

import com.fleetManagementSystem.commons.tracker.dto.MinimalTrackerResponse;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class VehicleRequest {


    @NotBlank(message = "License plate should not be blank")
    private String licensePlate;

    @NotBlank(message = "Model of the vehicle should not be blank")
    private String modelVehicle;

    @NotBlank(message = "Brand of the vehicle should not be blank")
    private String brandVehicle;

    @Min(value = 1886, message = "Year should not be less than 1886") // The first car was made in 1886
    @Max(value = 2100, message = "Year should not be greater than 2100") // Set an arbitrary future limit
    private int year;

    @NotBlank(message = "Color should not be blank")
    private String color;

    @NotBlank(message = "Fuel type should not be blank")
    @Pattern(regexp = "essence|diesel|electric|hybrid", message = "Fuel type must be one of: essence, diesel, electric, or hybrid")
    private String fuelType;

    @NotBlank(message = "Status should not be blank")
    @Pattern(regexp = "moving|stopped|in_maintenance|decommissioned", message = "Status must be one of: moving, stopped, in_maintenance, decommissioned")
    private String status;

    @NotBlank(message = "Organization type should not be blank")
    private String organization;


    @NotBlank(message = "vehicle type should not be blank")
    private String vehicleType;

    private String imgPath;


//    @NotNull(message = "Current driver information is required")
//    @Builder.Default
//    @Field("current_driver")
//    private SubVehicleCustomResponse.Driver currentDriver = new SubVehicleCustomResponse.Driver();

//    @NotNull(message = "Last position is required")
//@Builder.Default
//@Field("last_position")
//private SubVehicleCustomResponse.Position lastPosition = new SubVehicleCustomResponse.Position();

    @NotNull(message = "Tracker information is required")
    private MinimalTrackerResponse tracker;

}
