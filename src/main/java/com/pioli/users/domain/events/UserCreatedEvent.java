package com.pioli.users.domain.events;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.pioli.users.domain.aggregate.User;

import java.time.LocalDateTime;

@JsonAutoDetect(fieldVisibility = Visibility.ANY)
public class UserCreatedEvent implements DomainEvent {
    private final User user;
    private final LocalDateTime occurredOn;

    public UserCreatedEvent(User user) {
        this.user = user;
        this.occurredOn = LocalDateTime.now();
    }

    @Override
    public LocalDateTime occurredOn() {
        return occurredOn;
    }

    public User getUser() {
        return user;
    }
} 