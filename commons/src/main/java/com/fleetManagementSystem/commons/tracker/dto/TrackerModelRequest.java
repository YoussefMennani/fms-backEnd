package com.fleetManagementSystem.commons.tracker.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record TrackerModelRequest(

        String id,

        @NotBlank(message = "model name is required")
        String modelName,
        @NotBlank(message = "network type name is required")
        String networkType,
        @NotBlank(message = "network type name is required")
        String batteryLife,
        @NotNull(message = "features is required")
        List<String> features,
        @NotNull(message = "brand is required")
        String brand
        ) {

}
