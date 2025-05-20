package com.fleetManagementSystem.commons.subscription.model;

import com.fleetManagementSystem.commons.subscription.enums.PaymentMethod;
import com.fleetManagementSystem.commons.subscription.enums.PaymentStatus;
import com.fleetManagementSystem.commons.user.UserMinimal;
import jakarta.validation.constraints.DecimalMin;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@Document(collection = "payments")
@Builder
public class Payment {

    @Id
    private String id;

    @DecimalMin(value = "0.0", inclusive = false, message = "Amount must be positive")
    private BigDecimal amount;

    @DBRef(lazy = true)
    private UserMinimal userMinimal;

    private LocalDateTime paymentDate;

    private PaymentMethod paymentMethod;

    private PaymentStatus status;

    @DBRef(lazy = true)
    private Subscription subscription;

    @CreatedDate
    private Date createdAt;

    @LastModifiedDate
    private Date updatedAt;
}
