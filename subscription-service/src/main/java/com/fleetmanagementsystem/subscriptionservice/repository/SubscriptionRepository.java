package com.fleetmanagementsystem.subscriptionservice.repository;


import com.fleetManagementSystem.commons.subscription.model.Subscription;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface SubscriptionRepository extends MongoRepository<Subscription, String> {
    Subscription findByOrganizationId(String organizationId);

    List<Subscription> findSubscriptionsByOrganization(String organizationId);

    @Query("{ 'organization': ?0, 'startDate': { $lte: ?1 }, 'endDate': { $gte: ?1 } }")
    Subscription findActiveSubscription(String organizationId, Date currentDate);

}