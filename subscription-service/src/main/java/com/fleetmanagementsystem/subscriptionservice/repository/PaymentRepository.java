package com.fleetmanagementsystem.subscriptionservice.repository;

import com.fleetManagementSystem.commons.subscription.model.Payment;
import com.fleetManagementSystem.commons.subscription.model.Plan;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PaymentRepository extends MongoRepository<Payment,String> {

    List<Payment> findPaymentsBySubscriptionId(String subscriptionId);
}
