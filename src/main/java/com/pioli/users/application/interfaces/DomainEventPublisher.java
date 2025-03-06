package com.pioli.users.application.interfaces;

import com.pioli.users.domain.events.DomainEvent;

public interface DomainEventPublisher {
    void publish(String key, DomainEvent event);
} 