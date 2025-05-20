package com.fleetManagementSystem.commons.position.dto;

import com.fleetManagementSystem.commons.driver.dto.MinimalDriver;
import com.fleetManagementSystem.commons.position.model.Metrics;
import com.fleetManagementSystem.commons.vehicle.dto.MinimalVehicle;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class PositionVlResponse {
    private double latitude;
    private double longitude;
    private String imei;
    private double speed;
    private long timestamp;
    private double altitude;
    private double heading;
    private Metrics metrics;

}
