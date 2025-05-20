package com.fleetmanagementsystem.subscriptionservice.repository;


import com.fleetManagementSystem.commons.subscription.model.Subscription;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubscriptionRepository extends MongoRepository<Subscription, String> {
    Subscription findByOrganizationId(String organizationId);

    List<Subscription> findSubscriptionsByOrganization(String organizationId);
}