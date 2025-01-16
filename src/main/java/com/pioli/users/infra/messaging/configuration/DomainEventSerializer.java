package com.pioli.users.infra.messaging.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.pioli.users.domain.aggregate.User;
import com.pioli.users.domain.events.DomainEvent;
import com.pioli.users.domain.valueobject.Email;
import com.pioli.users.domain.valueobject.Name;
import com.pioli.users.infra.serialization.EmailMixin;
import com.pioli.users.infra.serialization.NameMixin;
import com.pioli.users.infra.serialization.UserMixin;

import org.apache.kafka.common.serialization.Serializer;

public class DomainEventSerializer implements Serializer<DomainEvent> {

    private final ObjectMapper objectMapper;

    public DomainEventSerializer() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.addMixIn(User.class, UserMixin.class);
        objectMapper.addMixIn(Name.class, NameMixin.class);
        objectMapper.addMixIn(Email.class, EmailMixin.class);
     }

    @Override
    public byte[] serialize(String topic, DomainEvent data) {
        try {
            return objectMapper.writeValueAsBytes(data);
        } catch (Exception e) {
            throw new RuntimeException("Error serializing DomainEvent", e);
        }
    }
} 