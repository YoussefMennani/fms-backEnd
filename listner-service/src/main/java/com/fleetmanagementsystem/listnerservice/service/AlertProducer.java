package com.fleetmanagementsystem.listnerservice.service;


import com.fleetManagementSystem.commons.position.model.Position;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class AlertProducer {

    private final KafkaTemplate<String, Position> kafkaTemplate;

    @Value("${kafka.topic.alert-topic}")
    private String topic;

    public AlertProducer(KafkaTemplate<String, Position> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendPosition(String topic, Position message) {
        kafkaTemplate.send(topic, message);
        System.out.println("Message sent to Kafka topic [" + topic + "]: " + message);
    }
}
