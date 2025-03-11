package com.pioli.users.infra.serialization;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.pioli.users.domain.aggregate.User;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

public class UserDeserializer extends JsonDeserializer<User> {
    @Override
    public User deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonNode node = p.getCodec().readTree(p);

        UUID id = UUID.fromString(node.get("id").asText());
        String name = node.get("name").asText();
        String email = node.get("email").asText();

        LocalDateTime createdAt = LocalDateTime.parse(node.get("createdAt").asText());
        LocalDateTime updatedAt = LocalDateTime.parse(node.get("updatedAt").asText());
        LocalDateTime deletedAt = node.has("deletedAt") && !node.get("deletedAt").isNull()
                ? LocalDateTime.parse(node.get("deletedAt").asText())
                : null;

        return User.load(id, name, email, null, createdAt, updatedAt, deletedAt);
    }
}