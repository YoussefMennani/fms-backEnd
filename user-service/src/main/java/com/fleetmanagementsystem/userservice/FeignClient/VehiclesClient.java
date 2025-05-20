package com.fleetmanagementsystem.userservice.FeignClient;

import com.fleetManagementSystem.commons.mapper.CustomResponse;
import com.fleetManagementSystem.commons.vehicle.dto.VehicleResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

@FeignClient(name = "vehicle-service", url = "http://localhost:8091/api/v1/vehicles")
public interface VehiclesClient {

    @GetMapping("/organization/{organization}")
    public ResponseEntity<CustomResponse<List<VehicleResponse>>> getAllVehiclesByOrganization(@RequestHeader("Authorization") String token,@PathVariable String organization);


}