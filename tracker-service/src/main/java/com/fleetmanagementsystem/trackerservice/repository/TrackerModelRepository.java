package com.fleetmanagementsystem.trackerservice.repository;

import com.fleetManagementSystem.commons.tracker.model.TrackerModel;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface TrackerModelRepository extends MongoRepository<TrackerModel,String> {
}
