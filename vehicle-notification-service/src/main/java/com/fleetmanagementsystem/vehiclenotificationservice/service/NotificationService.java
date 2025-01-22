package com.fleetmanagementsystem.vehiclenotificationservice.service;

import com.fleetManagementSystem.commons.alert.AlertVehicle;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final SimpMessagingTemplate messagingTemplate;

    public void sendNotification(String userId, AlertVehicle alertVehicle) {
        log.info("Sending WS notification to {} with payload {}", userId, alertVehicle);
        messagingTemplate.convertAndSendToUser(
                userId,
                "/notifications",
                alertVehicle
        );
    }
}
