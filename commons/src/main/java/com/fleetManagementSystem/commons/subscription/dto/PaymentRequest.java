package com.fleetManagementSystem.commons.subscription.dto;

import com.fleetManagementSystem.commons.subscription.model.CardDetails;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentRequest {
    private Long amount;
    private String paymentMethodId;
    private String subscriptionId;


}