package com.fleetmanagementsystem.subscriptionservice.repository;

import com.fleetManagementSystem.commons.subscription.model.Plan;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PlanRepository  extends MongoRepository<Plan,String> {
}
