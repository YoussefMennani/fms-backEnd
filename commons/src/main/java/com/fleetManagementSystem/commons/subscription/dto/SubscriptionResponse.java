package com.fleetManagementSystem.commons.subscription.dto;

import com.fleetManagementSystem.commons.organization.Organization;
import com.fleetManagementSystem.commons.subscription.model.Payment;
import com.fleetManagementSystem.commons.subscription.model.Plan;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class SubscriptionResponse {

    private String id;
    private Organization organization;
    private Plan plan;
    private List<Payment> paymentList;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private double discount;
    private double finalPrice;
    private boolean enabled;
}
