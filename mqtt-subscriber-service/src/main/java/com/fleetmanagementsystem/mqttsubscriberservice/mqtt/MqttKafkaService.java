package com.fleetmanagementsystem.mqttsubscriberservice.mqtt;

import com.fleetmanagementsystem.mqttsubscriberservice.mqtt.MqttMessageHandler;
import com.fleetmanagementsystem.mqttsubscriberservice.repository.PositionRepository;
import com.fleetmanagementsystem.mqttsubscriberservice.service.AlertProducer;
import com.fleetmanagementsystem.mqttsubscriberservice.service.PositionProducer;
import com.fleetmanagementsystem.mqttsubscriberservice.service.VehicleService;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.stereotype.Service;

@Service
public class MqttKafkaService {

    private static final String MQTT_BROKER_URL = "ssl://68a30ddca6044dd8a336d1f14f31b1b0.s1.eu.hivemq.cloud:8883";
    private static final String MQTT_CLIENT_ID = "spring-mqtt-kafka";
    private static final String MQTT_TOPIC = "gps/tracker";
    private static final String MQTT_USERNAME = "admin";
    private static final String MQTT_PASSWORD = "Admin2025";

    private final PositionProducer positionProducer;
    private final AlertProducer alertProducer;
    private final VehicleService vehicleService;
    private final PositionRepository positionRepository;

    public MqttKafkaService(PositionProducer positionProducer, AlertProducer alertProducer,
                            VehicleService vehicleService, PositionRepository positionRepository) {
        this.positionProducer = positionProducer;
        this.alertProducer = alertProducer;
        this.vehicleService = vehicleService;
        this.positionRepository = positionRepository;

        initializeMqttClient();
    }

    private void initializeMqttClient() {
        try {
            MqttClient client = new MqttClient(MQTT_BROKER_URL, MQTT_CLIENT_ID);
            MqttConnectOptions options = new MqttConnectOptions();
            options.setCleanSession(true);
            options.setUserName(MQTT_USERNAME);
            options.setPassword(MQTT_PASSWORD.toCharArray());
            client.connect(options);

            // Set the callback using the extracted handler
            client.setCallback(new MqttMessageHandler(positionProducer, alertProducer, vehicleService, positionRepository));

            // Subscribe to the topic
            client.subscribe(MQTT_TOPIC, 1);

        } catch (Exception e) {
            throw new RuntimeException("Error initializing MQTT client: " + e.getMessage(), e);
        }
    }
}
