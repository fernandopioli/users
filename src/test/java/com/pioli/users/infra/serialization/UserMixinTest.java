package com.pioli.users.infra.serialization;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.pioli.users.domain.aggregate.User;
import com.pioli.users.domain.valueobject.Email;
import com.pioli.users.domain.valueobject.Name;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

class UserMixinTest {

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());
        objectMapper.addMixIn(User.class, UserMixin.class);
        objectMapper.addMixIn(Name.class, NameMixin.class);
        objectMapper.addMixIn(Email.class, EmailMixin.class);
    }

    @Test
    void shouldSerializeUserWithoutPasswordAndDomainEvents() throws Exception {
        User user = User.create("John Doe", "john@example.com", "securePassword123");

        String json = objectMapper.writeValueAsString(user);
        Map<String, Object> jsonMap = objectMapper.readValue(json, new TypeReference<Map<String, Object>>() {
        });

        assertThat(jsonMap).containsKeys("id", "name", "email", "createdAt", "updatedAt");
        assertThat(jsonMap).doesNotContainKeys("password", "domainEvents");
        assertThat(jsonMap.get("name")).isEqualTo("John Doe");
        assertThat(jsonMap.get("email")).isEqualTo("john@example.com");
    }

    @Test
    void shouldDeserializeUserCorrectly() throws Exception {
        String id = "550e8400-e29b-41d4-a716-446655440000";
        String json = String.format(
                "{\"id\":\"%s\",\"name\":\"John Doe\",\"email\":\"john@example.com\",\"createdAt\":\"2023-01-01T12:00:00\",\"updatedAt\":\"2023-01-01T12:00:00\"}",
                id);

        User user = objectMapper.readValue(json, User.class);

        assertThat(user.getId().toString()).isEqualTo(id);
        assertThat(user.getName().getValue()).isEqualTo("John Doe");
        assertThat(user.getEmail().getValue()).isEqualTo("john@example.com");
        assertThat(user.getPassword()).isNull();
    }
}