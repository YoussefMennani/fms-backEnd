package com.fleetmanagementsystem.alertservice.service;

import com.fleetManagementSystem.commons.alert.AlertVehicle;
import com.fleetManagementSystem.commons.alert.NotificationStatus;
import com.fleetManagementSystem.commons.geofence.model.Geofence;
import com.fleetManagementSystem.commons.position.model.Position;
import com.fleetManagementSystem.commons.vehicle.model.Rule;
import com.fleetManagementSystem.commons.vehicle.model.RuleGroup;
import com.fleetmanagementsystem.alertservice.feignClient.GeofencingFeignClient;
import com.fleetmanagementsystem.alertservice.geofencing.PolygonService;
import feign.FeignException;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class AlertService {
    // private final JavaMailSender mailSender;
    private final KafkaTemplate<String, String> kafkaTemplate; // KafkaTemplate for producing messages

    private final EmailService emailService;
    private final GeofencingFeignClient geofencingFeignClient;

    private final PolygonService polygonService;
    private static boolean state = false;

    @KafkaListener(topics = "alert-topic", groupId = "alert-service-group")
    public void listen(Position position) {

        System.out.println("Received Position: " + position);


        if (position.getVehicle() != null && position.getVehicle().getRuleGroupList().size() > 0) {
            for (RuleGroup ruleGroup : position.getVehicle().getRuleGroupList()) {

                Map<String, Object> notificationWarningList = new HashMap<>();
                Map<String, Object> emailWarningList = new HashMap<>();
                Map<String, Object> alertList = new HashMap<>();


                for (Rule rule : ruleGroup.getRules()) {
                    if (rule.getFactor().equals("speed")) {

                        String operator = rule.getOperator(); // This can be dynamically set
                        int ruleValue = Integer.parseInt(rule.getValue());
                        double positionSpeed = position.getSpeed();
                        boolean condition = false;

                        switch (operator) {
                            case ">":
                                condition = positionSpeed > ruleValue;
                                break;
                            case ">=":
                                condition = positionSpeed >= ruleValue;
                                break;
                            case "<":
                                condition = positionSpeed < ruleValue;
                                break;
                            case "<=":
                                condition = positionSpeed <= ruleValue;
                                break;
                            case "==":
                                condition = positionSpeed == ruleValue;
                                break;
                            case "!=":
                                condition = positionSpeed != ruleValue;
                                break;
                            default:
                                throw new IllegalArgumentException("Invalid operator: " + operator);
                        }
                        if (condition) {

                            for (String action : rule.getActions()) {
                                switch (action) {
                                    case "send_notification":
                                        notificationWarningList.put("speed", "The speed exceeded the limit rule with " + positionSpeed + " Km/h");
                                        break;
                                    case "send_email":
                                        emailWarningList.put("speed", "The speed exceeded the limit rule with " + positionSpeed + " Km/h");
                                        break;
                                    default:
                                        System.out.println("UnSupported  method yet : " + action);
                                }
                            }
                        }
                    } else if (rule.getFactor().equals("geoFency")) {
                        String operator = rule.getOperator(); // This can be dynamically set
                        String polygonId = rule.getPolygon();
                        try {
                            Geofence geoFence = geofencingFeignClient.getGeoFenceById(polygonId);
                            if (geoFence == null) {
                                System.out.println("Geofence not found for ID: " + polygonId);
                            } else {
                                Boolean isPointIn = polygonService.isPointInPolygon(position.getLatitude(), position.getLongitude(), geoFence.getLocations());
                                if (isPointIn && operator.equals("inside") || !isPointIn && operator.equals("outside")) {
                                    for (String action : rule.getActions()) {
                                        switch (action) {
                                            case "send_notification":
                                                notificationWarningList.put("circumscription", "Vehicle is " + (isPointIn ? " IN " : " OUT ") + geoFence.getLabel());
                                                break;  // Prevents fall-through
                                            case "send_email":
                                                emailWarningList.put("circumscription", "Vehicle is " + (isPointIn ? " IN " : " OUT ") + geoFence.getLabel());
                                                break;
                                            default:
                                                System.out.println("Unsupported method yet: " + action);
                                        }
                                    }

                                }
                            }
                        } catch (FeignException.NotFound e) {
                            System.out.println("Geofence not found: " + e.getMessage());
                            return;
                        }
                    }

                }

                if (notificationWarningList.keySet().size() > 0) {
                    //create Alert  Object
                    AlertVehicle alertVehicle = new AlertVehicle(position.getVehicle().getId(), position.getVehicle().getLicensePlate(), position.getImei(), notificationWarningList, position.getTimestamp(), NotificationStatus.UNREAD,ruleGroup.getUsers());
                    emailService.notifyVehicleAlert(alertVehicle);

                    //state = true;
                }


            }
        }


        //Notification With email
//        if (emailWarningList.keySet().size() > 0) {
//            alertList.put("alerts", emailWarningList);
//            alertList.put("licensePlate", position.getVehicle().getLicensePlate());
//            alertList.put("date", position.getTimestamp());
//            alertList.put("latitude", position.getLatitude());  // Exemple de latitude
//            alertList.put("longitude", position.getLongitude());  // Exemple de longitude
//            processAlert(alertList);
//        }

        //Notification Web App

    }


//    public void notifyVehicle(String username, String licensePlate, String imeiTracker, Map<String, Object> alertList, Long timestamp) {
//        AlertVehicle alertVehicle = new AlertVehicle(username, licensePlate, imeiTracker, alertList, timestamp, NotificationStatus.UNREAD);
//        emailService.notifyVehicleAlert(alertVehicle);
//    }

    private void processAlert(Map<String, Object> alertList) {
        // sendEmail("admin@example.com", "High Speed Alert", position.toString());
        try {
            Date date = new Date((Long) alertList.get("date"));

            // Create SimpleDateFormat object with desired format
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

            // Format the date to the desired format
            String formattedDate = sdf.format(date);

            alertList.put("date", formattedDate);

            emailService.sendEmail("youssef.mennani@um5r.ac.ma", "GeoTrack Alert", alertList);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

//        // Push notification to Kafka
//        String username = "john_doe"; // Replace with actual logic to determine the user
//        String notificationMessage = formatNotification(alertList);
//        pushNotificationToKafka(username, notificationMessage);

    }

//    private String formatNotification(Map<String, Object> alertList) {
//        // Create a simple notification message
//        return "Alerts for vehicle " + alertList.get("licensePlate") + ": " + alertList.get("alerts").toString();
//    }

//    private void pushNotificationToKafka(String username, String notificationMessage) {
//        String message = username + ":" + notificationMessage;
//        kafkaTemplate.send("notify-topic", message);
//        log.info("Notification sent to Kafka: {}", message);
//    }


}


