package com.fleetmanagementsystem.trackerservice.repository;

import com.fleetManagementSystem.commons.tracker.model.TrackerBrand;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface TrackerBrandRepository extends MongoRepository<TrackerBrand,String> {
}
