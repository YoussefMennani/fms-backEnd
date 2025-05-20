package com.fleetmanagementsystem.positionservice.Repository;

import com.fleetManagementSystem.commons.position.model.Position;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PositionRepository extends MongoRepository<Position,String> {

//    @Query("{ 'vehicleID': ?0, 'timestamp': { $gte: ?1, $lte: ?2 } }")
//    List<Position> findByVehicleIDAndTimestampBetween(String vehicleID, Long timestampStart, Long timestampEnd);

    @Query("{ 'vehicle._id': ?0, 'timestamp': { $gte: ?1, $lte: ?2 } }")
    List<Position> findByVehicleIDAndTimestampBetween(String vehicleID, Long timestampStart, Long timestampEnd);


}
