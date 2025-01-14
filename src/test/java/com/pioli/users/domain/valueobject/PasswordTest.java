package com.pioli.users.domain.valueobject;

import com.pioli.users.domain.exceptions.InvalidParameterException;
import com.pioli.users.domain.exceptions.RequiredParameterException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PasswordTest {

    @Test
    void shouldCreateValidPassword() {
        Password password = Password.of("hashedPassword123");
        assertEquals("hashedPassword123", password.getValue());
    }

    @Test
    void shouldThrowExceptionForNullPassword() {
        Exception exception = assertThrows(RequiredParameterException.class, () -> {
            Password.of(null);
        });
        assertEquals("Field 'password' is required and cannot be empty", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionForShortPassword() {
        Exception exception = assertThrows(InvalidParameterException.class, () -> {
            Password.of("12345");
        });
        assertEquals("Field 'password' must have at least 6 characters", exception.getMessage());
    }

    @Test
    void shouldTestEquality() {
        Password password1 = Password.of("hashedPass");
        Password password2 = Password.of("hashedPass");
        Password password3 = Password.of("differentHash");

        assertEquals(password1, password2);
        assertNotEquals(password1, password3);
    }
} 