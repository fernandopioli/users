package com.pioli.users.infra.messaging.configuration;


import org.springframework.scheduling.annotation.Async;

import com.pioli.users.domain.events.DomainEvent;
import com.pioli.users.domain.events.DomainEventPublisher;

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
