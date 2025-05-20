package com.fleetmanagementsystem.anomaly_detection;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class AnomalyDetectionApplication {

    public static void main(String[] args) {
        SpringApplication.run(AnomalyDetectionApplication.class, args);
    }

}
