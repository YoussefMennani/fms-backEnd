package com.fleetManagementSystem.commons.alert;

import java.util.Map;


public record AlertVehicle(String username, String licensePlate, String imeiTracker, Map<String,Object> listAlert, Long timestamp,
                           NotificationStatus notificationStatus) {
}
