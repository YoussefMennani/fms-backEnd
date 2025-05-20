package com.fleetManagementSystem.commons.subscription.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PaymentRequest {
    private String productName;
    private BigDecimal amount;
    // Getters and Setters
}