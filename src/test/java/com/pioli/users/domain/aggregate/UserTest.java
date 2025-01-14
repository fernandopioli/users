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

    // @Test
    // void shouldThrowExceptionWhenNameIsInvalid() {
    //     Exception exception = assertThrows(InvalidParameterException.class, () -> {
    //         User.create("ab", "valid@example.com", "validPassword");
    //     });
    //     assertEquals("Field 'name' must have at least 3 characters", exception.getMessage());
    // }

    // @Test
    // void shouldThrowExceptionWhenEmailIsInvalid() {
    //     Exception exception = assertThrows(InvalidParameterException.class, () -> {
    //         User.create("Valid Name", "invalid-email", "validPassword");
    //     });
    //     assertEquals("Invalid email format", exception.getMessage());
    // }

    // @Test
    // void shouldThrowExceptionWhenPasswordIsInvalid() {
    //     Exception exception = assertThrows(InvalidParameterException.class, () -> {
    //         User.create("Valid Name", "valid@example.com", "123");
    //     });
    //     assertEquals("Field 'password' must have at least 6 characters", exception.getMessage());
    // }

    @Test
    void shouldUpdateOnlyProvidedFields() {
        user.update("Updated Name", null, null);

        assertEquals("Updated Name", user.getName().getValue());
        assertEquals("original@example.com", user.getEmail().getValue());
        assertEquals("originalPassword", user.getPassword().getValue());
    }

    // @Test
    // void shouldValidateFieldsWhenUpdating() {
    //     Exception exception = assertThrows(InvalidParameterException.class, () -> {
    //         user.update("ab", null, null);
    //     });
    //     assertEquals("Field 'name' must have at least 3 characters", exception.getMessage());

    //     exception = assertThrows(InvalidParameterException.class, () -> {
    //         user.update("Valid Name", "invalid-email", null);
    //     });
    //     assertEquals("Invalid email format", exception.getMessage());

    //     exception = assertThrows(InvalidParameterException.class, () -> {
    //         user.update("Valid Name", "valid@example.com", "123");
    //     });
    //     assertEquals("Field 'password' must have at least 6 characters", exception.getMessage());
    // }

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
            deletedAt
        );

        assertEquals(id, loadedUser.getId());
        assertEquals("Loaded Name", loadedUser.getName().getValue());
        assertEquals("loaded@example.com", loadedUser.getEmail().getValue());
        assertEquals("loadedPassword", loadedUser.getPassword().getValue());
        assertEquals(createdAt, loadedUser.getCreatedAt());
        assertEquals(updatedAt, loadedUser.getUpdatedAt());
        assertNull(loadedUser.getDeletedAt());
    }

    // @Test
    // void shouldThrowExceptionWhenNameIsNull() {
    //     Exception exception = assertThrows(RequiredParameterException.class, () -> {
    //         User.create(null, "valid@example.com", "validPassword");
    //     });
    //     assertEquals("Field 'name' is required and cannot be empty", exception.getMessage());
    // }

    // @Test
    // void shouldThrowExceptionWhenEmailIsNull() {
    //     Exception exception = assertThrows(RequiredParameterException.class, () -> {
    //         User.create("Valid Name", null, "validPassword");
    //     });
    //     assertEquals("Field 'email' is required and cannot be empty", exception.getMessage());
    // }

    // @Test
    // void shouldThrowExceptionWhenPasswordIsNull() {
    //     Exception exception = assertThrows(RequiredParameterException.class, () -> {
    //         User.create("Valid Name", "valid@example.com", null);
    //     });
    //     assertEquals("Field 'password' is required and cannot be empty", exception.getMessage());
    // }

    // @Test
    // void shouldThrowExceptionWhenUpdatingWithEmptyName() {
    //     Exception exception = assertThrows(RequiredParameterException.class, () -> {
    //         user.update("", null, null);
    //     });
    //     assertEquals("Field 'name' is required and cannot be empty", exception.getMessage());
    // }

    // @Test
    // void shouldThrowExceptionWhenUpdatingWithEmptyEmail() {
    //     Exception exception = assertThrows(RequiredParameterException.class, () -> {
    //         user.update(null, "", null);
    //     });
    //     assertEquals("Field 'email' is required and cannot be empty", exception.getMessage());
    // }

    // @Test
    // void shouldThrowExceptionWhenUpdatingWithEmptyPassword() {
    //     Exception exception = assertThrows(RequiredParameterException.class, () -> {
    //         user.update(null, null, "");
    //     });
    //     assertEquals("Field 'password' is required and cannot be empty", exception.getMessage());
    // }

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
}
