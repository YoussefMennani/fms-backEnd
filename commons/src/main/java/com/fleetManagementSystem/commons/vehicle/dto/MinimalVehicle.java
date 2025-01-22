package com.fleetManagementSystem.commons.vehicle.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class MinimalVehicle {

    private String id;

    private String licensePlate;
}
