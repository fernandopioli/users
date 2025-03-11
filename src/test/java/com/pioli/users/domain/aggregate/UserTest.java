package com.pioli.users.domain.aggregate;

import java.time.LocalDateTime;
import java.util.UUID;

import com.pioli.users.domain.base.Aggregate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

    private User user;

    @BeforeEach
    void setUp() {
        user = User.create("Original Name", "original@example.com", "originalPassword");
    }

    @Test
    void shouldCreateUserWithValidData() {
        assertTrue(user instanceof Aggregate);
        assertNotNull(user.getId());
        assertEquals("Original Name", user.getName().getValue());
        assertEquals("original@example.com", user.getEmail().getValue());
        assertEquals("originalPassword", user.getPassword().getValue());
    }

    @Test
    void shouldUpdateOnlyProvidedFields() {
        user.update("Updated Name", null, null);

        assertEquals("Updated Name", user.getName().getValue());
        assertEquals("original@example.com", user.getEmail().getValue());
        assertEquals("originalPassword", user.getPassword().getValue());
    }

    @Test
    void shouldDeleteUserSuccessfully() {
        user.delete();

        assertTrue(user.isDeleted());
        assertNotNull(user.getDeletedAt());
    }

    @Test
    void shouldLoadUserSuccessfully() {
        UUID id = UUID.randomUUID();
        LocalDateTime createdAt = LocalDateTime.now().minusDays(1);
        LocalDateTime updatedAt = LocalDateTime.now();
        LocalDateTime deletedAt = null;

        User loadedUser = User.load(
                id,
                "Loaded Name",
                "loaded@example.com",
                "loadedPassword",
                createdAt,
                updatedAt,
                deletedAt);

        assertEquals(id, loadedUser.getId());
        assertEquals("Loaded Name", loadedUser.getName().getValue());
        assertEquals("loaded@example.com", loadedUser.getEmail().getValue());
        assertEquals("loadedPassword", loadedUser.getPassword().getValue());
        assertEquals(createdAt, loadedUser.getCreatedAt());
        assertEquals(updatedAt, loadedUser.getUpdatedAt());
        assertNull(loadedUser.getDeletedAt());
    }

    @Test
    void shouldNotUpdateWhenUserParamsAreNull() {
        LocalDateTime originalUpdatedAt = user.getUpdatedAt();
        user.update(null, null, null);

        assertEquals("Original Name", user.getName().getValue());
        assertEquals("original@example.com", user.getEmail().getValue());
        assertEquals("originalPassword", user.getPassword().getValue());
        assertEquals(originalUpdatedAt, user.getUpdatedAt());
    }

    @Test
    void shouldNotUpdateUpdatedAtWhenDelete() {
        User user = User.create("Name", "email@example.com", "password123");
        LocalDateTime beforeDeleteUpdatedAt = user.getUpdatedAt();

        user.delete();

        assertNotNull(user.getDeletedAt());
        assertEquals(beforeDeleteUpdatedAt, user.getUpdatedAt());
    }

    @Test
    void shouldLoadUserWithNullPassword() {
        UUID id = UUID.randomUUID();
        LocalDateTime createdAt = LocalDateTime.now().minusDays(1);
        LocalDateTime updatedAt = LocalDateTime.now();

        User loadedUser = User.load(
                id,
                "Test Name",
                "test@example.com",
                null, // Null password
                createdAt,
                updatedAt,
                null);

        assertEquals(id, loadedUser.getId());
        assertEquals("Test Name", loadedUser.getName().getValue());
        assertEquals("test@example.com", loadedUser.getEmail().getValue());
        assertNull(loadedUser.getPassword()); // Password should be null
        assertEquals(createdAt, loadedUser.getCreatedAt());
        assertEquals(updatedAt, loadedUser.getUpdatedAt());
    }
}
