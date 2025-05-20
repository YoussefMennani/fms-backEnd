package com.fleetmanagementsystem.userservice.FeignClient;

import com.fleetManagementSystem.commons.driver.model.DriverResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

@FeignClient(name = "driver-service", url = "http://localhost:8097/api/v1/drivers")
public interface DriversClient {

    @GetMapping("")
    ResponseEntity<List<DriverResponse>> getAllDrivers(@RequestHeader("Authorization") String token);

}