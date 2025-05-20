package com.fleetmanagementsystem.vehiclesservice.mapper;


import com.fleetManagementSystem.commons.organization.Organization;
import com.fleetManagementSystem.commons.position.dto.PositionVlResponse;
import com.fleetManagementSystem.commons.position.model.Metrics;
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
                .vehicleType(vehicleRequest.getVehicleType())
//                .currentDriver(vehicleRequest.getCurrentDriver())
//                .lastPosition(vehicleRequest.getLastPosition())

                .tracker(new MinimalTrackerResponse(vehicleRequest.getTracker().getImei(),vehicleRequest.getTracker().getTrackerId()))
                .build();
    }

    public VehicleResponse toResponseVehicle(Vehicle vehicle) {

        PositionVlResponse positionVlResponse = PositionVlResponse.builder()
                .latitude(vehicle.getLastPosition().getLatitude())
                .longitude(vehicle.getLastPosition().getLongitude())
                .imei(vehicle.getLastPosition().getImei())
                .speed(vehicle.getLastPosition().getSpeed())
                .timestamp(vehicle.getLastPosition().getTimestamp())
                .altitude(vehicle.getLastPosition().getAltitude())
                .heading(vehicle.getLastPosition().getHeading())
                .metrics(vehicle.getLastPosition().getMetrics())
                .build();

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
                .lastPosition(positionVlResponse)
                .isMoving(vehicle.isMoving())
                .tracker(vehicle.getTracker())
                .organization(vehicle.getOrganization())
                .vehicleType(vehicle.getVehicleType())
                .imgPath(vehicle.getImgPath())
                .ruleGroupList(vehicle.getRuleGroupList())
                .build();
    }


}
