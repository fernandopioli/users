package com.pioli.users.domain.events;

import java.time.LocalDateTime;

public interface DomainEvent {
    LocalDateTime occurredOn();
} 