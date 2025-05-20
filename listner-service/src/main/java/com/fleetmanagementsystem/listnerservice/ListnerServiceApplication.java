package com.fleetmanagementsystem.listnerservice;

import com.fleetmanagementsystem.listnerservice.service.GPSSocketService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
public class ListnerServiceApplication implements CommandLineRunner {

//	public static void main(String[] args) {
//		SpringApplication.run(ListnerServiceApplication.class, args);
//	}


	private final GPSSocketService gpsSocketService;

	public ListnerServiceApplication(GPSSocketService gpsSocketService) {
		this.gpsSocketService = gpsSocketService;
	}

	public static void main(String[] args) {
		SpringApplication.run(ListnerServiceApplication.class, args);
	}

	@Override
	public void run(String... args) {
		gpsSocketService.startSocketServer();
	}

}
