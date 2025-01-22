package com.fleetmanagementsystem.positionservice.service;



import com.fleetManagementSystem.commons.geofence.model.Geofence;
import com.fleetManagementSystem.commons.geofence.model.Location;
import com.fleetmanagementsystem.positionservice.Repository.GeofenceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GeofenceService {

    @Autowired
    private GeofenceRepository geofenceRepository;

    public Geofence saveGeofence(String username, Long timestamp, List<Location> locations,String label, String color) {
        Geofence geofence = new Geofence(username, timestamp, locations,label,color);
        return geofenceRepository.save(geofence);
    }

    public Geofence updateGeofence(String id, String label, String color) {
        Geofence geofence = geofenceRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("Geofence not found."));

        if (label != null && !label.isEmpty()) {
            geofence.setLabel(label);
        }
        if (color != null && !color.isEmpty()) {
            geofence.setColor(color);
        }
        return geofenceRepository.save(geofence);
    }



    public void deleteGeofence(String id) {
        if (!geofenceRepository.existsById(id)) {
            throw new IllegalArgumentException("Geofence not found.");
        }
        geofenceRepository.deleteById(id);
    }

    // Method to get geofences by username
    public List<Geofence> getGeofencesByUsername(String username) {
        try {
            List<Geofence> list = geofenceRepository.findByUsername(username);
            return list;
        }catch (Exception ex){
            System.out.println(ex);
        }
        return null;
    }

}
