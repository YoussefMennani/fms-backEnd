package com.fleetmanagementsystem.vehiclesservice.repository;

import com.fleetManagementSystem.commons.vehicle.model.Vehicle;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface VehicleRepository extends MongoRepository<Vehicle,String> {

    Optional<Vehicle> findByLicensePlate(String licensePlate);

    Optional<Vehicle> findByTrackerImei(String imei);

    Optional<List<Vehicle>> findByOrganization(String organization);
}
