package com.fleetmanagementsystem.positionservice.Repository;


import com.fleetManagementSystem.commons.geofence.model.Geofence;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GeofenceRepository extends MongoRepository<Geofence, String> {
    // Custom query method to find Geofence by username
    List<Geofence> findByUsername(String username);
}
