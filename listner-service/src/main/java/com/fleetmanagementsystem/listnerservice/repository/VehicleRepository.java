package com.fleetmanagementsystem.listnerservice.repository;

import com.fleetManagementSystem.commons.vehicle.model.Vehicle;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VehicleRepository extends MongoRepository<Vehicle,String> {
    // Query to find a vehicle by the imei in the tracker field
    Optional<Vehicle> findByTrackerImei(String imei);

}
