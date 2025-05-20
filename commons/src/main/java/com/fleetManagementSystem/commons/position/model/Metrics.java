package com.fleetManagementSystem.commons.position.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Metrics {
    private double engineTemperature;
    private double coolantTemperature;
    private double fuelLevel;
    private int engineRPM;
    private double batteryVoltage;
    private boolean checkEngineLight;
    private double oilPressure;
    private double tirePressure;

    private boolean engineRunning;
}
