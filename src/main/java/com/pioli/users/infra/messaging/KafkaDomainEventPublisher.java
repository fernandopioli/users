package com.pioli.users.infra.messaging;

import com.pioli.users.application.interfaces.DomainEventPublisher;
import com.pioli.users.domain.events.DomainEvent;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;

public class KafkaDomainEventPublisher implements DomainEventPublisher {

    private final KafkaTemplate<String, DomainEvent> kafkaTemplate;
    private final String topic;

    public KafkaDomainEventPublisher(
            KafkaTemplate<String, DomainEvent> kafkaTemplate,
            @Value("${spring.kafka.topic.user-created}") String topic) {
        this.kafkaTemplate = kafkaTemplate;
        this.topic = topic;
    }

    @Override
    public void publish(String key, DomainEvent event) {
        kafkaTemplate.send(topic, key, event).whenComplete((result, ex) -> {
            if (ex != null) {
                System.err.println("Failed to publish message to topic " + topic);
                ex.printStackTrace();
            } else {
                System.out.println("Message published successfully to topic " + topic);
            }
        });
    }
} 