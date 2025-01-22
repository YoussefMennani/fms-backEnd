package com.fleetmanagementsystem.trackerservice.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class TrackerEntityNotFoundException extends RuntimeException {
    private final String msg;
}
