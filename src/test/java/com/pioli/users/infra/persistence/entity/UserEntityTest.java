package com.pioli.users.infra.persistence.entity;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.UUID;

import org.junit.jupiter.api.Test;


class UserEntityTest {

    @Test
    void shouldCreateUserEntityWithNoArgsConstructor() {
        UserEntity entity = new UserEntity();

        assertNull(entity.getId());
        assertNull(entity.getName());
        assertNull(entity.getEmail());
        assertNull(entity.getPassword());
        assertNull(entity.getCreatedAt());
        assertNull(entity.getUpdatedAt());
        assertNull(entity.getDeletedAt());
    }

    @Test
    void shouldCreateUserEntityWithAllArgsConstructor() {
        UUID id = UUID.randomUUID();
        String name = "Any name";
        String email = "mail@mail.com";
        String password = "anyPassword";
        LocalDateTime createdAt = LocalDateTime.now().minusDays(1);
        LocalDateTime updatedAt = LocalDateTime.now();
        LocalDateTime deletedAt = LocalDateTime.now().minusHours(1);

        UserEntity entity = new UserEntity(
            id,
            name,
            email,
            password,
            createdAt,
            updatedAt,
            deletedAt
        );

        assertEquals(id, entity.getId());
        assertEquals(name, entity.getName());
        assertEquals(email, entity.getEmail());
        assertEquals(password, entity.getPassword());
        assertEquals(createdAt, entity.getCreatedAt());
        assertEquals(updatedAt, entity.getUpdatedAt());
        assertEquals(deletedAt, entity.getDeletedAt());
    }

    @Test
    void shouldSetAndGetAllFields() {
        UserEntity entity = new UserEntity();

        UUID id = UUID.randomUUID();
        String name = "Any name";
        String email = "mail@mail.com";
        String password = "anyPassword";
        LocalDateTime createdAt = LocalDateTime.now().minusDays(1);
        LocalDateTime updatedAt = LocalDateTime.now();
        LocalDateTime deletedAt = LocalDateTime.now().minusHours(1);

        entity.setId(id);
        entity.setName(name);
        entity.setEmail(email);
        entity.setPassword(password);
        entity.setCreatedAt(createdAt);
        entity.setUpdatedAt(updatedAt);
        entity.setDeletedAt(deletedAt);

        assertEquals(id, entity.getId());
        assertEquals(name, entity.getName());
        assertEquals(email, entity.getEmail());
        assertEquals(password, entity.getPassword());
        assertEquals(createdAt, entity.getCreatedAt());
        assertEquals(updatedAt, entity.getUpdatedAt());
        assertEquals(deletedAt, entity.getDeletedAt());
    }
}
