package com.pioli.users.domain.validation;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

import com.pioli.users.domain.exceptions.InvalidParameterException;
import com.pioli.users.domain.exceptions.RequiredParameterException;

public class ValidatorTest {
    @Test
    void shouldThrowExceptionForNullValue() {
        RequiredParameterException exceptionNull = assertThrows(RequiredParameterException.class, () -> {
            Validator.validateRequired((Map<String, Object>) null);
        });
        assertEquals("No fields provided for validation", exceptionNull.getMessage());

        RequiredParameterException exceptionEmpty = assertThrows(RequiredParameterException.class, () -> {
            Validator.validateRequired(Map.of());
        });
        assertEquals("No fields provided for validation", exceptionEmpty.getMessage());
    }
    @Test
    void shouldThrowExceptionForNullOrEmptyValues() {
        RequiredParameterException exceptionNull = assertThrows(RequiredParameterException.class, () -> {
            Map<String, Object> fields = new HashMap<>();
            fields.put("name", null);
            Validator.validateRequired(fields);
        });
        assertEquals("Field 'name' has an invalid value: 'null'", exceptionNull.getMessage());

        RequiredParameterException exceptionEmpty = assertThrows(RequiredParameterException.class, () -> {
            Validator.validateRequired(Map.of("email", ""));
        });
        assertEquals("Field 'email' has an invalid value: ''", exceptionEmpty.getMessage());

        RequiredParameterException exceptionSpace = assertThrows(RequiredParameterException.class, () -> {
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

    @Test
    void checkMinLengthShouldNotThrowWhenValueIsLongEnough() {
        assertDoesNotThrow(() -> Validator.checkMinLength("abcdef", 6, "password"));
    }

    @Test
    void checkMinLengthShouldThrowWhenValueIsTooShort() {
        InvalidParameterException exception = assertThrows(
            InvalidParameterException.class,
            () -> Validator.checkMinLength("abc", 6, "password")
        );

        assertTrue(exception.getMessage().contains("Field 'password' must have at least 6 characters"));
    }

    @Test
    void checkMinLengthShouldThrowWhenValueIsNull() {
        InvalidParameterException exception = assertThrows(
            InvalidParameterException.class,
            () -> Validator.checkMinLength(null, 6, "password")
        );

        assertTrue(exception.getMessage().contains("Field 'password' must have at least 6 characters"));
    }

    @Test
    void validateEmailFormatShouldNotThrowForValidEmail() {
        assertDoesNotThrow(() -> Validator.validateEmailFormat("john.doe@example.com"));
        assertDoesNotThrow(() -> Validator.validateEmailFormat("user123+any@subdomain.domain.co"));
        assertDoesNotThrow(() -> Validator.validateEmailFormat("user.name@domain.org"));
    }

    @Test
    void validateEmailFormatShouldThrowForMissingAtSymbol() {
        InvalidParameterException exception = assertThrows(
            InvalidParameterException.class,
            () -> Validator.validateEmailFormat("invalidemail.com")
        );
        assertEquals("Invalid email format", exception.getMessage());
    }

    @Test
    void validateEmailFormatShouldThrowForMissingUsername() {
        InvalidParameterException exception = assertThrows(
            InvalidParameterException.class,
            () -> Validator.validateEmailFormat("@example.com")
        );
        assertEquals("Invalid email format", exception.getMessage());
    }

    @Test
    void validateEmailFormatShouldThrowForMissingDomain() {
        InvalidParameterException exception = assertThrows(
            InvalidParameterException.class,
            () -> Validator.validateEmailFormat("username@.com")
        );
        assertEquals("Invalid email format", exception.getMessage());
    }

    @Test
    void validateEmailFormatShouldThrowForNullEmail() {
        InvalidParameterException exception = assertThrows(
            InvalidParameterException.class,
            () -> Validator.validateEmailFormat(null)
        );
        assertEquals("Invalid email format", exception.getMessage());
    }

    @Test
    void validateEmailFormatShouldThrowForEmptyString() {
        InvalidParameterException exception = assertThrows(
            InvalidParameterException.class,
            () -> Validator.validateEmailFormat("")
        );
        assertEquals("Invalid email format", exception.getMessage());
    }
}
