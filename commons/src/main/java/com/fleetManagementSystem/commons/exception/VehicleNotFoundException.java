package com.fleetManagementSystem.commons.exception;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class VehicleNotFoundException extends RuntimeException{
    private final String msg;
}
