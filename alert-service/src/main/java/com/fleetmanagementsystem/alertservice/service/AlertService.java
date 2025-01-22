package com.fleetmanagementsystem.alertservice.service;

import com.fleetManagementSystem.commons.alert.AlertVehicle;
import com.fleetManagementSystem.commons.alert.NotificationStatus;
import com.fleetManagementSystem.commons.geofence.model.Geofence;
import com.fleetManagementSystem.commons.position.model.Position;
import com.fleetmanagementsystem.alertservice.email.EmailTemplates;
import com.fleetmanagementsystem.alertservice.feignClient.GeofencingFeignClient;
import com.fleetmanagementsystem.alertservice.geofencing.PolygonService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import jakarta.mail.internet.MimeMessage;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;

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
//    private final SpringTemplateEngine templateEngine;

    @KafkaListener(topics = "alert-topic", groupId = "alert-service-group")
    public void listen(Position position) {

        System.out.println("Received Position: " + position);

        Map<String, Object> warningList = new HashMap<>();
        Map<String, Object> alertList = new HashMap<>();

        if (position.getSpeed() > 120) {
            warningList.put("speed", "the speed exceeded the limit rule with " + position.getSpeed() + "Km/h");
        }
        if (position.getMetrics().isCheckEngineLight()) {
            warningList.put("check Engine", "CheckEngine Light is ON ");
        }
        if (position.getMetrics().getCoolantTemperature() > 100) {
            warningList.put("Coolant Temperature", "exceed limit 100 *C");
        }


        List<Geofence> geofenceList = geofencingFeignClient.getGeofencesByUsername(position.getDriver().getOrganization());
        if (geofenceList.size() > 0) {
            geofenceList.forEach((Geofence geoFence) -> {
//                33.89495, -5.562017
                Boolean isPointIn = polygonService.isPointInPolygon(position.getLatitude(), position.getLongitude(), geoFence.getLocations());
                if (isPointIn) {
                    warningList.put("circumscription", "Vehicle is " + (isPointIn ? " IN " : " OUT ") + geoFence.getLabel());
//                    notifyVehicle("admin@mail.com",position.getVehicle().getLicensePlate(),position.getImei(),warningList,position.getTimestamp());
                    notifyVehicle(position.getVehicle().getId(), position.getVehicle().getLicensePlate(), position.getImei(), warningList, position.getTimestamp());

                }
            });
        }

        if (warningList.keySet().size() > 0) {
            alertList.put("alerts", warningList);
            alertList.put("licensePlate", position.getVehicle().getLicensePlate());
            alertList.put("date", position.getTimestamp());
            alertList.put("latitude", position.getLatitude());  // Exemple de latitude
            alertList.put("longitude", position.getLongitude());  // Exemple de longitude
            processAlert(alertList);
            notifyVehicle(position.getVehicle().getId(), position.getVehicle().getLicensePlate(), position.getImei(), warningList, position.getTimestamp());
            //state = true;
        }

    }


    public void notifyVehicle(String username, String licensePlate, String imeiTracker, Map<String, Object> alertList, Long timestamp) {
        AlertVehicle alertVehicle = new AlertVehicle(username, licensePlate, imeiTracker, alertList, timestamp, NotificationStatus.UNREAD);
        emailService.notifyVehicleAlert(alertVehicle);
    }

    private void processAlert(Map<String, Object> alertList) {
        // sendEmail("admin@example.com", "High Speed Alert", position.toString());
        try {
            Date date = new Date((Long) alertList.get("date"));

            // Create SimpleDateFormat object with desired format
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

            // Format the date to the desired format
            String formattedDate = sdf.format(date);

            alertList.put("date", formattedDate);

            emailService.sendOrderConfirmationEmail("youssef.mennani@um5r.ac.ma", "GeoTrack Alert", alertList);
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


