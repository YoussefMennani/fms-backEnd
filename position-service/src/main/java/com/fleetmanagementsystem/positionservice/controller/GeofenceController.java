package com.fleetmanagementsystem.positionservice.controller;



import com.fleetManagementSystem.commons.geofence.dto.GeofenceRequest;
import com.fleetManagementSystem.commons.geofence.model.Geofence;
import com.fleetmanagementsystem.positionservice.service.GeofenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/positions/geofence")
//@CrossOrigin("*") // Enable CORS for this controller
public class GeofenceController {

    @Autowired
    private GeofenceService geofenceService;

    @PostMapping("/create")
    public Geofence createGeofence(@RequestBody GeofenceRequest geofenceRequest) {
        return geofenceService.saveGeofence(
                geofenceRequest.getUsername(),
                geofenceRequest.getTimestamp(),
                geofenceRequest.getLocations(),
                geofenceRequest.getLabel(),
                geofenceRequest.getColor()
        );
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateGeofence(@PathVariable String id, @RequestBody GeofenceRequest geofenceRequest) {
        try {
            Geofence geofence = geofenceService.updateGeofence(
                    id,
                    geofenceRequest.getLabel(),
                    geofenceRequest.getColor()
            );
            return ResponseEntity.ok(geofence);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteGeofence(@PathVariable String id) {
        try {
            geofenceService.deleteGeofence(id);
            return ResponseEntity.ok(id);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
        }
    }


    // GET endpoint to retrieve geofences by username
    @GetMapping("/by-username/{username}")
    public List<Geofence> getGeofencesByUsername(@PathVariable String username) {
        List<Geofence> geofenceList = geofenceService.getGeofencesByUsername(username);
        return geofenceList;
    }
    @GetMapping("/{idGeoFence}")
    public Geofence getGeoFenceById(@PathVariable String idGeoFence) {
        Geofence geofence = geofenceService.getGeofenceById(idGeoFence);
        return geofence;
    }
}
