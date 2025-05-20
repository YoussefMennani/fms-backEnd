package com.fleetmanagementsystem.mqttsubscriberservice.mqtt;

import com.fleetManagementSystem.commons.driver.dto.MinimalDriver;
import com.fleetManagementSystem.commons.exception.VehicleNotFoundException;
import com.fleetManagementSystem.commons.position.model.Position;
import com.fleetManagementSystem.commons.vehicle.dto.MinimalVehicle;
import com.fleetManagementSystem.commons.vehicle.model.Vehicle;
import com.fleetmanagementsystem.mqttsubscriberservice.parser.NMEAParser;
import com.fleetmanagementsystem.mqttsubscriberservice.repository.PositionRepository;
import com.fleetmanagementsystem.mqttsubscriberservice.service.AlertProducer;
import com.fleetmanagementsystem.mqttsubscriberservice.service.PositionProducer;
import com.fleetmanagementsystem.mqttsubscriberservice.service.VehicleService;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.Date;

public class MqttMessageHandler implements MqttCallback {

    private final PositionProducer positionProducer;
    private final AlertProducer alertProducer;
    private final VehicleService vehicleService;
    private final PositionRepository positionRepository;

    public MqttMessageHandler(PositionProducer positionProducer, AlertProducer alertProducer,
                              VehicleService vehicleService, PositionRepository positionRepository) {
        this.positionProducer = positionProducer;
        this.alertProducer = alertProducer;
        this.vehicleService = vehicleService;
        this.positionRepository = positionRepository;
    }

    @Override
    public void connectionLost(Throwable cause) {
        System.out.println("MQTT connection lost: " + cause.getMessage());
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) {
        String payload = new String(message.getPayload());
        System.out.println("Received MQTT message: " + payload);

        try {
            // Parse payload into a Position
            Position position = NMEAParser.parseNMEA(payload);
            position.setTimestamp(new Date().getTime());

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
                alertProducer.sendPosition("alert-topic", position);
            }
            // Update the vehicle's last position
            vehicleService.updateVehicleLastPosition(vehicle.getId(), position);

            System.out.println("Position processed and saved: " + savedPosition);

        } catch (Exception e) {
            System.err.println("Error processing MQTT message: " + e.getMessage());
        }
    }

    @Override
    public void deliveryComplete(org.eclipse.paho.client.mqttv3.IMqttDeliveryToken token) {
        // No action needed for subscriber
    }
}
