package com.fleetmanagementsystem.subscriptionservice.controller;

import com.fleetManagementSystem.commons.subscription.dto.PaymentRequest;
import com.fleetManagementSystem.commons.subscription.enums.PaymentMethod;
import com.fleetManagementSystem.commons.subscription.enums.PaymentStatus;
import com.fleetManagementSystem.commons.subscription.model.Payment;
import com.fleetmanagementsystem.subscriptionservice.service.StripeApiPaymentGateway;
import com.stripe.exception.StripeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/payment")
public class PaymentController {

    @Autowired
    private StripeApiPaymentGateway paymentService;

    @PostMapping("/process")
    public ResponseEntity<Payment> processPayment(@RequestBody PaymentRequest paymentRequest) throws StripeException {
        Payment payment = Payment.builder()
                .amount(paymentRequest.getAmount())
                .paymentMethod(PaymentMethod.STRIPE)
                .status(PaymentStatus.PENDING)
                .userMinimal(null)
                .subscriptionId(paymentRequest.getSubscriptionId())
                .build();

        Payment paymentResponse = paymentService.processPaymentWithPaymentMethod(
                payment,
                paymentRequest.getPaymentMethodId()
        );
        return ResponseEntity.ok(paymentResponse);
    }
}