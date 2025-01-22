package com.fleetmanagementsystem.vehiclesservice.mapper;


import com.fleetManagementSystem.commons.organization.Organization;
import com.fleetManagementSystem.commons.tracker.dto.MinimalTrackerResponse;
import com.fleetManagementSystem.commons.vehicle.dto.VehicleRequest;
import com.fleetManagementSystem.commons.vehicle.dto.VehicleResponse;
import com.fleetManagementSystem.commons.vehicle.model.Vehicle;
import com.fleetmanagementsystem.vehiclesservice.FeignClient.OrganizationClient;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
@RequiredArgsConstructor
public class VehicleMapper {

    private final OrganizationClient organizationClient;
    public Vehicle toVehicle(VehicleRequest vehicleRequest){


        JwtAuthenticationToken authentication = (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        String token = "Bearer " + authentication.getToken().getTokenValue();
        Organization organization = organizationClient.getOrganizationById(token,vehicleRequest.getOrganization()).getBody();

        return Vehicle.builder()
                .licensePlate(vehicleRequest.getLicensePlate())
                .modelVehicle(vehicleRequest.getModelVehicle())
                .brandVehicle(vehicleRequest.getBrandVehicle())
                .year(vehicleRequest.getYear())
                .color(vehicleRequest.getColor())
                .fuelType(vehicleRequest.getFuelType())
                .status(vehicleRequest.getStatus())
                .organization(organization)
                .imgPath(vehicleRequest.getImgPath())
//                .currentDriver(vehicleRequest.getCurrentDriver())
//                .lastPosition(vehicleRequest.getLastPosition())

                .tracker(new MinimalTrackerResponse(vehicleRequest.getTracker().getImei(),vehicleRequest.getTracker().getTrackerId()))
                .build();
    }

    public VehicleResponse toResponseVehicle(Vehicle vehicle) {
        return VehicleResponse.builder()
                .id(vehicle.getId())
                .licensePlate(vehicle.getLicensePlate())
                .modelVehicle(vehicle.getModelVehicle())
                .brandVehicle(vehicle.getBrandVehicle())
                .year(vehicle.getYear())
                .color(vehicle.getColor())
                .fuelType(vehicle.getFuelType())
                .status(vehicle.getStatus())
                .currentDriver(vehicle.getCurrentDriver())
                .lastPosition(vehicle.getLastPosition())
                .isMoving(vehicle.isMoving())
                .tracker(vehicle.getTracker())
                .organization(vehicle.getOrganization().getName())
                .imgPath(vehicle.getImgPath())
                .build();
    }
}
