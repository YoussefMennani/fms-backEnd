package com.fleetmanagementsystem.vehiclesservice.FeignClient;

import com.fleetManagementSystem.commons.driver.model.Driver;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "driver-service", url = "http://localhost:8097/api/v1/drivers")
public interface DriverClient {

    @GetMapping("/{id}")
    ResponseEntity<Driver> getDriverById(@PathVariable String id);

    @PutMapping("/{id}")
    ResponseEntity<Driver> updateAvailabilityDriver(@RequestHeader("Authorization") String token,@PathVariable String id,@RequestBody Boolean available);

}
