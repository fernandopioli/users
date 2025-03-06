package com.pioli.users.infra.messaging.configuration;


import org.springframework.scheduling.annotation.Async;

import com.pioli.users.application.interfaces.DomainEventPublisher;
import com.pioli.users.domain.events.DomainEvent;

public class AsyncDomainEventPublisher implements DomainEventPublisher {

    private final DomainEventPublisher delegate;

    public AsyncDomainEventPublisher(DomainEventPublisher delegate) {
        this.delegate = delegate;
    }

    @Async
    @Override
    public void publish(String key, DomainEvent event) {
        delegate.publish(key, event);
    }
}
