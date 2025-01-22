package com.fleetmanagementsystem.vehiclesservice.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class VehicleEntityNotFoundException extends RuntimeException {

    private final String msg;
}
