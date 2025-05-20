package com.fleetManagementSystem.commons.tracker.dto;

import com.fleetManagementSystem.commons.enums.GPSStatus;
import com.fleetManagementSystem.commons.organization.Organization;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record TrackerRequest(

        String id,

        @NotBlank(message = "IMEI is required")
        @Pattern(regexp = "\\d{15}", message = "IMEI must be exactly 15 digits")
        String imei,



        @NotBlank(message = "Tracker model is required")
        String model,

        @NotBlank(message = "Tracker brand is required")
        String brand,

        @NotBlank(message = "Sim Serial Number is required")
        @Size(min = 19, max = 20, message = "Sim Serial Number must be 19-20 characters")
        String simSerialNumber,

        @NotBlank(message = "Sim Number is required")
        @Pattern(regexp = "^(06|07)\\d{8}$", message = "Sim Number must start with 06 or 07 and be 10 digits long")
        String simNumber,

        @NotNull(message = "Status is required")
        GPSStatus status,

        @NotBlank(message = "Organization is required")
        String organization

) {

}
