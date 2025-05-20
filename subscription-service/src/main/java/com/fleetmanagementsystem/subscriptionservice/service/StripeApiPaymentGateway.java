package com.fleetmanagementsystem.subscriptionservice.service;

import com.fleetManagementSystem.commons.subscription.dto.PaymentRequest;
import com.fleetManagementSystem.commons.subscription.enums.PaymentStatus;
import com.fleetManagementSystem.commons.subscription.model.Payment;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class StripeApiPaymentGateway {
    @Value("${stripe.api.secret-key}")
    private String secretKey;

    @PostConstruct
    public void init() {
        Stripe.apiKey = secretKey;
    }


    public Payment processPayment(Payment payment){

        Map<String, Object> chargeParams =  new HashMap<>();
        chargeParams.put("amount",payment.getAmount());
        chargeParams.put("currency","mad");
        chargeParams.put("description","Test payment for booking service");
        chargeParams.put("source","tok_in");

        try {
            Charge.create(chargeParams);
            payment.setStatus(PaymentStatus.CONFIRMED);
        } catch (StripeException e) {
            log.error("Error encountered during payment process : "+e.getMessage());
            payment.setStatus(PaymentStatus.CANCELED);
        }

        return payment;

    }
}
