package com.fleetmanagementsystem.reellistnerservice.service;

import com.fleetManagementSystem.commons.driver.dto.MinimalDriver;
import com.fleetManagementSystem.commons.exception.VehicleNotFoundException;
import com.fleetManagementSystem.commons.position.model.Position;

import com.fleetManagementSystem.commons.vehicle.dto.MinimalVehicle;
import com.fleetManagementSystem.commons.vehicle.model.Vehicle;
import com.fleetmanagementsystem.reellistnerservice.parser.NMEAParser;
import com.fleetmanagementsystem.reellistnerservice.repository.PositionRepository;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class TrackerService {


    private int port = 5000;


    private final PositionRepository positionRepository;
    private final PositionProducer positionProducer;

    private final AlertProducer alertProducer;
    private final VehicleService vehicleService; // Injected here
    private final Gson gson;




    @Autowired
    public TrackerService(PositionRepository positionRepository,
                          PositionProducer positionProducer,
                          VehicleService vehicleService,
                          AlertProducer alertProducer
                            ) {
        this.positionRepository = positionRepository;
        this.positionProducer = positionProducer;
        this.vehicleService = vehicleService;
        this.alertProducer = alertProducer;
        this.gson = new Gson();
    }


    public void handlePosition(String packet){

        System.out.println(packet);

        Position position = NMEAParser.parseNMEA(packet);
        System.out.println("Position ==> "+position);
        position.setTimestamp( new Date().getTime());
        Vehicle vehicle = vehicleService.findVehicleByImei(position.getImei())
                .orElseThrow(() -> new VehicleNotFoundException("Vehicle Not Found"));

        //Vehicle DATA
        MinimalVehicle minimalVehicle = MinimalVehicle.builder()
                .id(vehicle.getId().toString())
                .licensePlate(vehicle.getLicensePlate())
                .build();
        position.setVehicle(minimalVehicle);

        //DRIVER DATA
        MinimalDriver minimalDriver = MinimalDriver.builder()
                .id(vehicle.getCurrentDriver().getId())
                .firstName(vehicle.getCurrentDriver().getFirstName())
                .lastName(vehicle.getCurrentDriver().getLastName())
                .phoneNumber(vehicle.getCurrentDriver().getPhoneNumber())
                .organization(vehicle.getCurrentDriver().getOrganization().getName())
                .build();

        position.setDriver(minimalDriver);
        // Save to MongoDB
        positionRepository.save(position);
        System.out.println("Position saved to MongoDB: " + position);

        // Send to Kafka position
        positionProducer.sendPosition("position-updates", position);
        // Send to Kafka
        alertProducer.sendPosition("alert-topic", position);

        // update lastPosition
        vehicleService.updateVehicleLastPosition(vehicle.getId(),position);

    }


}
