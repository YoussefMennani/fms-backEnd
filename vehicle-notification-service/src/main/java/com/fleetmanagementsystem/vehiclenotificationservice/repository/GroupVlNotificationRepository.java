package com.fleetmanagementsystem.vehiclenotificationservice.repository;


import com.fleetManagementSystem.commons.groupVehicleNotifications.GroupVlNotification;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupVlNotificationRepository extends MongoRepository<GroupVlNotification, String> {
    // Find groups by vehicle ID
    List<GroupVlNotification> findByVehiclesContaining(String vehicleId);
}