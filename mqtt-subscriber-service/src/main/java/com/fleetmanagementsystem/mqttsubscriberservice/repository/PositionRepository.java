package com.fleetmanagementsystem.mqttsubscriberservice.repository;


import com.fleetManagementSystem.commons.position.model.Position;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PositionRepository extends MongoRepository<Position, String> {
}
