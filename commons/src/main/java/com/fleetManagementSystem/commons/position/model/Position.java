package com.fleetManagementSystem.commons.position.model;

import com.fleetManagementSystem.commons.driver.dto.MinimalDriver;
import com.fleetManagementSystem.commons.vehicle.dto.MinimalVehicle;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "positions")
@ToString
public class Position {
    private double latitude;
    private double longitude;
    private String imei;
    private double speed;
    private long timestamp;
    private double altitude;
    private double heading;
    private Metrics metrics;
    private MinimalVehicle vehicle;
    private MinimalDriver driver;

    @CreatedDate
    private Date createdAt;

    @LastModifiedDate
    private Date updatedAt;

}
