package com.pioli.users.infra.messaging.configuration;

import com.pioli.users.application.interfaces.DomainEventPublisher;
import com.pioli.users.domain.events.DomainEvent;
import com.pioli.users.infra.messaging.KafkaDomainEventPublisher;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.EnableAsync;

@Configuration
@EnableAsync
public class DomainEventPublisherConfig {

    @Bean
    public DomainEventPublisher domainEventPublisher(
            KafkaTemplate<String, DomainEvent> kafkaTemplate,
            @Value("${spring.kafka.topic.user-created}") String topic) {
                return new AsyncDomainEventPublisher(
                    new KafkaDomainEventPublisher(kafkaTemplate, topic)
            );
    }
} 