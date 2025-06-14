package com.fleetmanagementsystem.driverservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication
@EnableDiscoveryClient
@EnableMongoAuditing
@EnableFeignClients
public class DriverServiceApplication {

    public static void main(String[] args) {
        SSLValidationDisabler.disableSSLVerification();
        SpringApplication.run(DriverServiceApplication.class, args);
    }

}
