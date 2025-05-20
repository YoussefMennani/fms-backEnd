package com.fleetmanagementsystem.positionservice.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fleetManagementSystem.commons.driver.dto.MinimalDriver;
import com.fleetManagementSystem.commons.exception.PositionNotFoundException;
import com.fleetManagementSystem.commons.exception.VehicleNotFoundException;
import com.fleetManagementSystem.commons.position.model.Position;
import com.fleetManagementSystem.commons.vehicle.dto.MinimalVehicle;
import com.fleetManagementSystem.commons.vehicle.model.Vehicle;
import com.fleetmanagementsystem.positionservice.Repository.PositionRepository;
import com.fleetmanagementsystem.positionservice.kafka.AlertProducer;
import com.fleetmanagementsystem.positionservice.kafka.PositionProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PositionService {

    private final PositionRepository positionRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final VehicleService vehicleService;
    private final AlertProducer alertProducer;
    private final PositionProducer positionProducer;

    @KafkaListener(topics = "position", groupId = "gps-consumer-group")
    public void consumePositionUpdates(String message) throws Exception {
        System.out.println("Received message: " + message);  // Add logging for the raw message
        // Deserialize JSON string to Position object
        Position position = objectMapper.readValue(message, Position.class);
        System.out.println("Received position update: " + position);

        try {

            Vehicle vehicle = vehicleService.findVehicleByImei(position.getImei())
                    .orElseThrow(() -> new VehicleNotFoundException("Vehicle Not Found"));

            MinimalVehicle minimalVehicle = MinimalVehicle.builder()
                    .id(vehicle.getId().toString())
                    .licensePlate(vehicle.getLicensePlate())
                    .ruleGroupList(vehicle.getRuleGroupList())
                    .build();
            position.setVehicle(minimalVehicle);

            position.setVehicle(minimalVehicle);


            if (vehicle.getCurrentDriver() != null) {
                //DRIVER DATA
                MinimalDriver minimalDriver = MinimalDriver.builder()
                        .id(vehicle.getCurrentDriver().getId())
                        .firstName(vehicle.getCurrentDriver().getFirstName())
                        .lastName(vehicle.getCurrentDriver().getLastName())
                        .phoneNumber(vehicle.getCurrentDriver().getPhoneNumber())
                        .organization(vehicle.getOrganization().getId())
                        .build();
                position.setDriver(minimalDriver);
            } else {
                position.setDriver(null);
            }


            Position savedPosition = positionRepository.save(position);

            // Send messages to Kafka
            positionProducer.sendPosition("anomaly-detection", position);
            positionProducer.sendPosition("position-updates", position);
            if(position.getVehicle().getRuleGroupList() != null && position.getVehicle().getRuleGroupList().size() > 0){
            // alertProducer.sendPosition("alert-topic", position);
                positionProducer.sendPosition("alert-topic", position);
            }
            // Update the vehicle's last position
            vehicleService.updateVehicleLastPosition(vehicle.getId(), position);

            System.out.println("Position processed and saved: " + savedPosition);

        } catch (Exception e) {
            System.err.println("Error processing MQTT message: " + e.getMessage());
        }

    }

    public List<Position> findPositionsByVehicleAndDateRange(String vehicleID, Long timestampStart, Long timestampEnd) {
        // Query the database for matching positions
        List<Position> positions = positionRepository.findByVehicleIDAndTimestampBetween(vehicleID, timestampStart, timestampEnd);
        return positions;
    }

    public Position findPositionById(String positionId) {
        Position position = positionRepository.findById(positionId).orElseThrow(()-> new PositionNotFoundException("position not found"));
        return position;
    }
}
