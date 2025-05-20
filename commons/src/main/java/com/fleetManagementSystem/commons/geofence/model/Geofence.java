package com.fleetManagementSystem.commons.geofence.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "geofencing_data")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Geofence {

    @Id
    private String id;

    private String username;
    private Long timestamp;

    private String label;

    private String color;
    private List<Location> locations;



    // Constructors, Getters and Setters for Geofence class

    public Geofence(String username, Long timestamp, List<Location> locations,String label, String color) {
        this.username = username;
        this.timestamp = timestamp;
        this.locations = locations;
        this.label = label;
        this.color = color;
    }

}
