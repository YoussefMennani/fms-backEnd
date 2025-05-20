package com.fleetmanagementsystem.positionservice.service;

import com.fleetManagementSystem.commons.position.dto.PositionVlResponse;
import com.fleetManagementSystem.commons.position.model.Position;
import com.fleetManagementSystem.commons.vehicle.model.Vehicle;
import com.fleetmanagementsystem.positionservice.FeignClient.VehicleFeignClient;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Optional;

@Service
public class VehicleService {

    private final VehicleFeignClient vehicleFeignClient;

    public VehicleService(VehicleFeignClient vehicleFeignClient) {
        this.vehicleFeignClient = vehicleFeignClient;
    }

    public Optional<Vehicle> findVehicleByImei(String imei) {
        try {
            System.out.println("Fetching vehicle with IMEI: " + imei);
            Vehicle vehicle = vehicleFeignClient.getVehicleByImei(imei);
            return Optional.of(vehicle);
        } catch (Exception e) {
            System.err.println("Error fetching vehicle: " + e.getMessage());
            return Optional.empty();
        }
    }

    public Optional<Vehicle>updateVehicleLastPosition(
            @PathVariable("vehicleId") String vehicleId,
            @RequestBody @Valid Position position
    ){
        try {

            PositionVlResponse positionVlResponse = PositionVlResponse.builder()
                    .latitude(position.getLatitude())
                    .longitude(position.getLongitude())
                    .imei(position.getImei())
                    .speed(position.getSpeed())
                    .timestamp(position.getTimestamp())
                    .altitude(position.getAltitude())
                    .heading(position.getHeading())
                    .metrics(position.getMetrics())
                    .build();

            System.out.println("update vehicle with ID : " + vehicleId);
            Vehicle vehicle = vehicleFeignClient.updateVehicleLastPosition(vehicleId,positionVlResponse);
            return Optional.of(vehicle);
        } catch (Exception e) {
            System.err.println("Error fetching vehicle: " + e.getMessage());
            return Optional.empty();
        }
    }

}
