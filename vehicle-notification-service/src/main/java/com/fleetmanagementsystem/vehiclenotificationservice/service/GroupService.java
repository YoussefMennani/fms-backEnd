package com.fleetmanagementsystem.vehiclenotificationservice.service;



import com.fleetManagementSystem.commons.groupVehicleNotifications.GroupVlNotification;
import com.fleetmanagementsystem.vehiclenotificationservice.repository.GroupVlNotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GroupService {

    @Autowired
    private GroupVlNotificationRepository GroupVlNotificationRepository;

    // Get users to notify for a given vehicle ID
    public List<String> getUsersToNotifyByVehicleId(String vehicleId) {
        List<GroupVlNotification> groups = GroupVlNotificationRepository.findByVehiclesContaining(vehicleId);
        List<String> usersToNotify = new ArrayList<>();

        // Collect all users from the groups
        for (GroupVlNotification group : groups) {
            usersToNotify.addAll(group.getUsers());
        }

        return usersToNotify;
    }
}