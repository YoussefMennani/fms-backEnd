package com.fleetmanagementsystem.driverservice.controller;

import com.fleetManagementSystem.commons.driver.model.Driver;
import com.fleetManagementSystem.commons.driver.model.DriverRequest;
import com.fleetManagementSystem.commons.driver.model.DriverResponse;
import com.fleetmanagementsystem.driverservice.service.DriverService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/drivers")
//@CrossOrigin(origins = "http://localhost:5173") // Enable CORS for this controller
@Validated
public class DriverController {

    @Autowired
    private DriverService driverService;

    @PostMapping
    public ResponseEntity<Driver> addDriver(@RequestBody DriverRequest driver) {
        Driver savedDriver = driverService.addDriver(driver);
        return ResponseEntity.ok(savedDriver);
    }

    @GetMapping
    public ResponseEntity<List<DriverResponse>> getAllDrivers() {
        List<DriverResponse> drivers = driverService.getAllDrivers();
        return ResponseEntity.ok(drivers);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DriverResponse> getDriverById(@PathVariable String id) {
        DriverResponse driver = driverService.getDriverById(id);
        return ResponseEntity.ok(driver);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Driver> updateAvailabilityDriver(@PathVariable String id,@RequestBody Boolean available){
        Driver driver = driverService.updateAvailabilityDriver(id,available);
        return ResponseEntity.ok(driver);
    }

}
