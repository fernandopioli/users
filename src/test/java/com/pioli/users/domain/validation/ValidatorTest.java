package com.pioli.users.domain.validation;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

public class ValidatorTest {
    @Test
    void shouldThrowExceptionForNullValue() {
        IllegalArgumentException exceptionNull = assertThrows(IllegalArgumentException.class, () -> {
            Validator.validateRequired((Map<String, Object>) null);
        });
        assertEquals("No fields provided for validation", exceptionNull.getMessage());

        IllegalArgumentException exceptionEmpty = assertThrows(IllegalArgumentException.class, () -> {
            Validator.validateRequired(Map.of());
        });
        assertEquals("No fields provided for validation", exceptionEmpty.getMessage());
    }
    @Test
    void shouldThrowExceptionForNullOrEmptyValues() {
        IllegalArgumentException exceptionNull = assertThrows(IllegalArgumentException.class, () -> {
            Map<String, Object> fields = new HashMap<>();
            fields.put("name", null);
            Validator.validateRequired(fields);
        });
        assertEquals("Field 'name' has an invalid value: 'null'", exceptionNull.getMessage());

        IllegalArgumentException exceptionEmpty = assertThrows(IllegalArgumentException.class, () -> {
            Validator.validateRequired(Map.of("email", ""));
        });
        assertEquals("Field 'email' has an invalid value: ''", exceptionEmpty.getMessage());

        IllegalArgumentException exceptionSpace = assertThrows(IllegalArgumentException.class, () -> {
            Validator.validateRequired(Map.of("password", " "));
        });
        assertEquals("Field 'password' has an invalid value: ' '", exceptionSpace.getMessage());
    }

    @Test
    void shouldNotThrowExceptionForValidValues() {
        assertDoesNotThrow(() -> {
            Validator.validateRequired(Map.of(
                "name", "John Doe",
                "email", "john.doe@example.com",
                "password", "securePassword123",
                "password2", 123456
            ));
        });
    }
}
