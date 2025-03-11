package com.pioli.users.domain.valueobject;

import org.junit.jupiter.api.Test;

import com.pioli.users.domain.exceptions.ValidationException;

import static org.junit.jupiter.api.Assertions.*;

public class EmailTest {

    @Test
    void shouldCreateValidEmail() {
        Email email = Email.of("User@Example.com");
        assertEquals("user@example.com", email.getValue());
    }

    @Test
    void shouldThrowExceptionForNullEmail() {
        Exception exception = assertThrows(ValidationException.class, () -> {
            Email.of(null);
        });
        assertEquals("Field 'email' is required and cannot be empty", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionForInvalidEmail() {
        Exception exception = assertThrows(ValidationException.class, () -> {
            Email.of("invalid-email");
        });
        assertEquals("Invalid email format", exception.getMessage());
    }

    @Test
    void shouldTestEquality() {
        Email email1 = Email.of("test@example.com");
        Email email2 = Email.of("TEST@example.com");
        Email email3 = Email.of("different@example.com");

        assertEquals(email1, email2);
        assertNotEquals(email1, email3);
    }
}