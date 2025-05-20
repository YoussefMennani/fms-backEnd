package com.fleetManagementSystem.commons.exception;


public class PlanNotFoundException extends RuntimeException {
    public PlanNotFoundException(String message) {
        super(message);
    }
}