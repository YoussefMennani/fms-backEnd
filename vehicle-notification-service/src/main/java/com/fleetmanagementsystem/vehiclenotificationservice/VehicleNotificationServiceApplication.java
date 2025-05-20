package com.fleetmanagementsystem.vehiclenotificationservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableDiscoveryClient
public class VehicleNotificationServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(VehicleNotificationServiceApplication.class, args);
    }

}
