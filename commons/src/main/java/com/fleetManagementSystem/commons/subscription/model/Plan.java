package com.fleetManagementSystem.commons.subscription.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "plans")
public class Plan {

    @Id
    private String id;

    @NotBlank(message = "Name is mandatory")
    private String name;

    @NotNull(message = "Price is mandatory")
    private Double price;

    @Min(value = 0, message = "Vehicles must be a positive number")
    private int vehicles;

    @Min(value = 0, message = "GPS Trackers must be a positive number")
    private int gpsTrackers;

    @Min(value = 0, message = "Users must be a positive number")
    private int users;
}