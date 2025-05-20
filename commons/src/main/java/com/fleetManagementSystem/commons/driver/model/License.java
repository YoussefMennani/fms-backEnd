package com.fleetManagementSystem.commons.driver.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class License {

    @NotBlank(message = "License number is mandatory")
    private String licenseNumber;

    @NotBlank(message = "License class is mandatory")
    private String licenseClass;

    @Min(value = 0, message = "The Birth Date must be a positive value.")
    @Max(value = 4102444800000L, message = "The Birth Date must not exceed the year 2100.")
    private Long issueDate;

    @Min(value = 0, message = "The Birth Date must be a positive value.")
    @Max(value = 4102444800000L, message = "The Birth Date must not exceed the year 2100.")
    private Long expiryDate;
}