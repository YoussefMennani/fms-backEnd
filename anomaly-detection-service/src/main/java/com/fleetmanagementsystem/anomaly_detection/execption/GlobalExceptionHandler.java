package com.fleetmanagementsystem.anomaly_detection.execption;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
        // Log the exception (optional)
        System.err.println("IllegalArgumentException occurred: " + ex.getMessage());

        // Return a custom response
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("Invalid argument: " + ex.getMessage());
    }

    // Additional handlers for other exceptions can go here
}
