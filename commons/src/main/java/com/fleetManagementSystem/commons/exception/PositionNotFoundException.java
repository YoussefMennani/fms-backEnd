package com.fleetManagementSystem.commons.exception;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PositionNotFoundException extends RuntimeException{
    private final String msg;
}
