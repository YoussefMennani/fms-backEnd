package com.fleetmanagementsystem.trackerservice.repository;

import com.fleetManagementSystem.commons.tracker.model.Tracker;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface TrackerRepository extends MongoRepository<Tracker,String> {
}
