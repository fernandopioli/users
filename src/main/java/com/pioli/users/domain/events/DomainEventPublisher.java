package com.pioli.users.domain.events;

public interface DomainEventPublisher {
    void publish(String key, DomainEvent event);
} 