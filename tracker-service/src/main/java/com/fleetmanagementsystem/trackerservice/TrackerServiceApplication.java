package com.fleetmanagementsystem.trackerservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.web.bind.annotation.CrossOrigin;

@SpringBootApplication
@EnableDiscoveryClient
@EnableMongoAuditing
//@CrossOrigin("*")
@EnableFeignClients
public class TrackerServiceApplication {

	public static void main(String[] args) {
		SSLValidationDisabler.disableSSLVerification();

		SpringApplication.run(TrackerServiceApplication.class, args);
	}


}
