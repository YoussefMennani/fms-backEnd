package com.fleetmanagementsystem.vehiclenotificationservice.service;

import com.fleetManagementSystem.commons.alert.AlertVehicle;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VehicleNotifConsumer {

    private final NotificationService notificationService;
    private final GroupService groupService;

    // This method listens to the "notify-topic" topic and processes the messages
    @KafkaListener(topics = "notify-topic", groupId = "your-consumer-group-id")
    public void listen(AlertVehicle alertVehicle) {
        System.out.println("Received message from topic  ===> " + alertVehicle);
//        alertVehicle.notificationStatus();
        List<String> listUsersToNotify  = groupService.getUsersToNotifyByVehicleId(alertVehicle.username());
        for (String userName : listUsersToNotify ) {
            notificationService.sendNotification(userName,alertVehicle);

        }
        // You can process the message here (e.g., sending a notification)
    }
}