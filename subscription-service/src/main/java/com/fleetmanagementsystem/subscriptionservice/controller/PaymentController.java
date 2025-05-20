package com.fleetmanagementsystem.subscriptionservice.controller;

import com.fleetManagementSystem.commons.subscription.dto.PaymentRequest;
import com.fleetManagementSystem.commons.subscription.enums.PaymentMethod;
import com.fleetManagementSystem.commons.subscription.enums.PaymentStatus;
import com.fleetManagementSystem.commons.subscription.model.Payment;
import com.fleetmanagementsystem.subscriptionservice.service.PaymentService;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

// 5. PaymentController.java
@RestController
@RequestMapping("/api/v1/payment")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;




    @PostMapping("/create-checkout-session")
    public ResponseEntity<Payment> createCheckoutSession(@RequestBody PaymentRequest paymentRequest) throws StripeException {
        Payment payment  = Payment.builder()
                .amount(paymentRequest.getAmount())
                .paymentMethod(PaymentMethod.STRIPE)
                .status(PaymentStatus.PENDING)
                .userMinimal(null)
                .build();
        Payment paymentResponse = paymentService.makePayment(payment);
        return ResponseEntity.ok(paymentResponse);
    }

//    @GetMapping("/success")
//    public String getSuccess(){
//        return "payment successful";
//    }
//
//    @GetMapping("/cancel")
//    public String cancel(){
//        return "payment canceled";
//    }
}