package com.fleetManagementSystem.commons.subscription.model;

import com.fleetManagementSystem.commons.organization.Organization;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(collection = "subscriptions")
public class Subscription {
    @Id
    private String id;

    @DBRef(lazy = true) // Lazy loading for organization
    private Organization organization;

    @DBRef(lazy = true) // Lazy loading for plan
    private Plan plan;

    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private double discount;
    private double finalPrice;
    private boolean enabled = true; // New field, enabled by default

}