package com.pioli.users.domain.base;

import com.pioli.users.domain.events.DomainEvent;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public abstract class Aggregate extends Entity {
    private final List<DomainEvent> domainEvents = new ArrayList<>();

    protected Aggregate() {
        super();
    }
    protected Aggregate(UUID id, LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime deletedAt) {
        super(id, createdAt, updatedAt, deletedAt);
    }

    protected void recordEvent(DomainEvent event) {
        domainEvents.add(event);
    }

    public List<DomainEvent> getDomainEvents() {
        return new ArrayList<>(domainEvents);
    }

    public void clearDomainEvents() {
        domainEvents.clear();
    }
}
