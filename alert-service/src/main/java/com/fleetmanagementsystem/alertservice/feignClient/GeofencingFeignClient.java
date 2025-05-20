package com.fleetmanagementsystem.alertservice.feignClient;


import com.fleetManagementSystem.commons.geofence.model.Geofence;
import com.fleetManagementSystem.commons.position.model.Position;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "position-service", url = "http://localhost:8092/api/v1/positions/geofence")
public interface GeofencingFeignClient {

    @GetMapping("/by-username/{username}")
    public List<Geofence> getGeofencesByUsername(@PathVariable String username);
    @GetMapping("/{idGeoFence}")
    Geofence getGeoFenceById(@PathVariable String idGeoFence);
}
