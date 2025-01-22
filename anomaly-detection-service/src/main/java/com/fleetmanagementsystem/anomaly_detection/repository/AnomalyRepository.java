package com.fleetmanagementsystem.anomaly_detection.repository;

import com.fleetmanagementsystem.anomaly_detection.model.AnomalyDetection;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface AnomalyRepository extends MongoRepository<AnomalyDetection,String> {
    List<AnomalyDetection> findByPositionDateBetween(LocalDateTime startDate, LocalDateTime endDate);

}
