package com.fleetmanagementsystem.kafkaservice.controller;


import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@EnableKafka
public class KafkaConsumerService {

    private final SimpMessagingTemplate messagingTemplate;

    // Inject SimpMessagingTemplate to send messages over WebSocket
    public KafkaConsumerService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    // Kafka Listener to consume messages from the "position-updates" topic
    @KafkaListener(topics = "position-updates", groupId = "position-group")
    public void consume(String message) {
        System.out.println(" =========> "+message);
        // Send the message received from Kafka to all WebSocket clients
        messagingTemplate.convertAndSend("/topic/positions", message);
    }
}
