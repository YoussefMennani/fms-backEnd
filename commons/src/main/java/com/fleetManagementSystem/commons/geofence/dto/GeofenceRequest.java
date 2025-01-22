package com.fleetManagementSystem.commons.geofence.dto;

import com.fleetManagementSystem.commons.geofence.model.Geofence;
import com.fleetManagementSystem.commons.geofence.model.Location;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GeofenceRequest {
    private String username;
    private Long timestamp;
    private List<Location> locations;
    private String label;
    private String color;

}