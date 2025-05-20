package com.fleetmanagementsystem.vehiclesservice.repository;

import com.fleetManagementSystem.commons.vehicle.model.RuleGroup;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RuleGroupRepository extends MongoRepository<RuleGroup, String> {
    Optional<List<RuleGroup>> getRuleGroupByUsername(String username);
}
