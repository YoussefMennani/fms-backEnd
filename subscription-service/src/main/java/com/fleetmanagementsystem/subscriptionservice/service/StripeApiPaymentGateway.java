package com.fleetmanagementsystem.subscriptionservice.service;

import com.fleetManagementSystem.commons.subscription.enums.PaymentStatus;
import com.fleetManagementSystem.commons.subscription.model.Payment;
import com.fleetmanagementsystem.subscriptionservice.repository.PaymentRepository;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.model.Charge;
import com.stripe.param.PaymentIntentCreateParams;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class StripeApiPaymentGateway {

    private final PaymentRepository paymentRepository;

//    @Value("${stripe.api.secret-key}")
    private String secretKey;

    public StripeApiPaymentGateway(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @PostConstruct
    public void init() {
        Stripe.apiKey = secretKey;
    }

    public Payment processPaymentWithPaymentMethod(Payment payment, String paymentMethodId) {
        try {
            PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                    .setAmount(payment.getAmount())
                    .setCurrency("mad")
                    .setPaymentMethod(paymentMethodId)
                    .setConfirm(true)
                    .setDescription("Payment for fleet management service")
                    .setAutomaticPaymentMethods(
                            PaymentIntentCreateParams.AutomaticPaymentMethods.builder()
                                    .setEnabled(true)
                                    .setAllowRedirects(PaymentIntentCreateParams.AutomaticPaymentMethods.AllowRedirects.NEVER)
                                    .build()
                    )
                    .build();

            PaymentIntent intent = PaymentIntent.create(params);

            if ("succeeded".equals(intent.getStatus())) {
                payment.setStatus(PaymentStatus.CONFIRMED);
                payment.setStripePaymentIntentId(intent.getId());

                // Retrieve receipt URL from latest charge
                String chargeId = intent.getLatestCharge();
                if (chargeId != null && !chargeId.isEmpty()) {
                    try {
                        Charge charge = Charge.retrieve(chargeId);
                        String receiptUrl = charge.getReceiptUrl();
                        payment.setReceiptUrl(receiptUrl);  // Make sure Payment has this field
                        log.info("Receipt URL: " + receiptUrl);
                    } catch (StripeException e) {
                        log.error("Failed to retrieve charge for receipt URL: " + e.getMessage());
                    }
                } else {
                    log.warn("No latest charge found for PaymentIntent: " + intent.getId());
                }

            } else {
                payment.setStatus(PaymentStatus.CANCELED);
                log.error("Payment failed: " + intent.getLastPaymentError());
            }
        } catch (StripeException e) {
            log.error("Stripe error: " + e.getMessage());
            payment.setStatus(PaymentStatus.CANCELED);
        }

        // Save the updated payment entity
        return paymentRepository.save(payment);
    }
}
