package com.pioli.users.domain.validation;

import static org.junit.jupiter.api.Assertions.*;

import com.pioli.users.domain.exceptions.InvalidParameterException;
import com.pioli.users.domain.exceptions.RequiredParameterException;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class ValidatorTest {

    @Test
    void shouldThrowExceptionWhenRequiredFieldsAreMissing() {
        Map<String, Object> fields = new HashMap<>();
        fields.put("name", null);
        fields.put("email", "");

        RequiredParameterException exception = assertThrows(RequiredParameterException.class, () -> {
            Validator.validateRequiredFields(fields);
        });
        assertEquals("Field 'name' is required and cannot be empty", exception.getMessage());
    }

    @Test
    void shouldValidateRequiredFieldSuccessfully() {
        assertDoesNotThrow(() -> {
            Validator.validateRequiredField("name", "Valid Name");
        });
    }

    @Test
    void shouldThrowExceptionWhenRequiredFieldIsNull() {
        RequiredParameterException exception = assertThrows(RequiredParameterException.class, () -> {
            Validator.validateRequiredField("email", null);
        });
        assertEquals("Field 'email' is required and cannot be empty", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenRequiredFieldIsEmpty() {
        RequiredParameterException exception = assertThrows(RequiredParameterException.class, () -> {
            Validator.validateRequiredField("email", "   ");
        });
        assertEquals("Field 'email' is required and cannot be empty", exception.getMessage());
    }

    @Test
    void shouldValidateEmailFormatSuccessfully() {
        assertDoesNotThrow(() -> {
            Validator.validateEmailFormat("valid@example.com");
        });
    }

    @Test
    void validateEmailFormatShouldNotThrowForValidEmail() {
        assertDoesNotThrow(() -> Validator.validateEmailFormat("john.doe@example.com"));
        assertDoesNotThrow(() -> Validator.validateEmailFormat("user123+any@subdomain.domain.co"));
        assertDoesNotThrow(() -> Validator.validateEmailFormat("user.name@domain.org"));
    }
    
    @Test
    void shouldThrowExceptionForInvalidEmailFormat() {
        InvalidParameterException exception = assertThrows(InvalidParameterException.class, () -> {
            Validator.validateEmailFormat("invalid-email");
        });
        assertEquals("Invalid email format", exception.getMessage());
    }

    @Test
    void shouldValidateMinLengthSuccessfully() {
        assertDoesNotThrow(() -> {
            Validator.checkMinLength("ValidName", 3, "name");
        });
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

    @Test
    void shouldThrowExceptionWhenValueBelowMinLength() {
        InvalidParameterException exception = assertThrows(InvalidParameterException.class, () -> {
            Validator.checkMinLength("ab", 3, "name");
        });
        assertEquals("Field 'name' must have at least 3 characters", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenRequiredMapIsEmpty() {
        Map<String, Object> fields = new HashMap<>();

        RequiredParameterException exception = assertThrows(RequiredParameterException.class, () -> {
            Validator.validateRequiredFields(fields);
        });
        assertEquals("No fields provided for validation", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenValueIsNullInCheckMinLength() {
        InvalidParameterException exception = assertThrows(InvalidParameterException.class, () -> {
            Validator.checkMinLength(null, 3, "name");
        });
        assertEquals("Field 'name' must have at least 3 characters", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenRequiredFieldsMapIsNull() {
        RequiredParameterException exception = assertThrows(RequiredParameterException.class, () -> {
            Validator.validateRequiredFields(null);
        });
        assertEquals("No fields provided for validation", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenRequiredFieldsMapIsEmpty() {
        Map<String, Object> fields = new HashMap<>();

        RequiredParameterException exception = assertThrows(RequiredParameterException.class, () -> {
            Validator.validateRequiredFields(fields);
        });
        assertEquals("No fields provided for validation", exception.getMessage());
    }

    @Test
    void shouldValidateRequiredFieldsSuccessfully() {
        Map<String, Object> fields = new HashMap<>();
        fields.put("field1", "value1");
        fields.put("field2", "value2");

        assertDoesNotThrow(() -> {
            Validator.validateRequiredFields(fields);
        });
    }

    @Test
    void shouldNotThrowExceptionWhenRequiredFieldIsNonStringObject() {
        Object nonStringValue = new Object();

        assertDoesNotThrow(() -> {
            Validator.validateRequiredField("testField", nonStringValue);
        });
    }

    @Test
    void shouldNotThrowExceptionWhenRequiredFieldIsNumber() {
        Integer numberValue = 42;

        assertDoesNotThrow(() -> {
            Validator.validateRequiredField("testField", numberValue);
        });
    }
}
