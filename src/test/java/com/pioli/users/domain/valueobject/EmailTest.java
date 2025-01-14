package com.pioli.users.domain.valueobject;

import com.pioli.users.domain.exceptions.InvalidParameterException;
import com.pioli.users.domain.exceptions.RequiredParameterException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class EmailTest {

    @Test
    void shouldCreateValidEmail() {
        Email email = Email.of("User@Example.com");
        assertEquals("user@example.com", email.getValue());
    }

    @Test
    void shouldThrowExceptionForNullEmail() {
        Exception exception = assertThrows(RequiredParameterException.class, () -> {
            Email.of(null);
        });
        assertEquals("Field 'email' is required and cannot be empty", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionForInvalidEmail() {
        Exception exception = assertThrows(InvalidParameterException.class, () -> {
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