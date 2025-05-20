package com.fleetManagementSystem.commons.tracker.dto;

import jakarta.validation.constraints.NotBlank;

public record TrackerBrandRequest(
        String id,
        @NotBlank(message = "brand origin country is required")
        String originCountry,
        @NotBlank(message = "brand name is required")
        String brandName


) {

}
