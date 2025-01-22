package com.fleetmanagementsystem.reellistnerservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
//implements CommandLineRunner
public class ReelListnerServiceApplication  {

    public static void main(String[] args) {
        SpringApplication.run(ReelListnerServiceApplication.class, args);
    }
//private final GPSSocketService gpsSocketService;
//
//    public ReelListnerServiceApplication(GPSSocketService gpsSocketService) {
//        this.gpsSocketService = gpsSocketService;
//    }
//
//    public static void main(String[] args) {
//        SpringApplication.run(ReelListnerServiceApplication.class, args);
//    }
//
//    @Override
//    public void run(String... args) {
//        gpsSocketService.startSocketServer();
//    }

}
