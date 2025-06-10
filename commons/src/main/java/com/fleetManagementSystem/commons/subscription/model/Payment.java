package com.fleetManagementSystem.commons.subscription.model;

import com.fleetManagementSystem.commons.subscription.enums.PaymentMethod;
import com.fleetManagementSystem.commons.subscription.enums.PaymentStatus;
import com.fleetManagementSystem.commons.user.UserMinimal;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "payments")
public class Payment {

    @Id
    private String id;

    @DecimalMin(value = "0.0", inclusive = false, message = "Amount must be positive")
    @NotNull(message = "Amount cannot be null")
    private Long amount;

    @DBRef(lazy = true)
    private UserMinimal userMinimal;

    private String subscriptionId;

    private LocalDateTime paymentDate;

    @NotNull(message = "Payment method cannot be null")
    private PaymentMethod paymentMethod;

    @NotNull(message = "Status cannot be null")
    private PaymentStatus status;

    @DBRef(lazy = true)
    private Subscription subscription;

    // Stripe-specific fields
    private String stripePaymentIntentId;
    private String stripeCustomerId;
    private String receiptUrl;

    // Metadata
    private String invoiceNumber;
    private String description;

    // Currency
    private String currency;

    // Auditing
    @CreatedDate
    private Date createdAt;

    @LastModifiedDate
    private Date updatedAt;

    // Processor response
    private String processorResponseCode;
    private String processorResponseText;

    // Refund details
    private boolean refunded;
    private BigDecimal refundAmount;
}
