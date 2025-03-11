package com.pioli.users.domain.validation;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class ValidatorTest {

    @Test
    void shouldReturnTrueIfRequiredFieldSuccessfully() {
        boolean result = Validator.validateRequiredField("name", "Valid Name");
        assertTrue(result);
    }

    @Test
    void shouldReturnFalseIfRequiredFieldIsNull() {
        boolean result = Validator.validateRequiredField("email", null);
        assertFalse(result);
    }

    @Test
    void shouldReturnFalseIfRequiredFieldIsEmpty() {
        boolean result = Validator.validateRequiredField("email", "   ");
        assertFalse(result);
    }

    @Test
    void shouldReturnTrueIfRequiredFieldIsNonStringObject() {
        Object nonStringValue = new Object();

        boolean result = Validator.validateRequiredField("testField", nonStringValue);
        assertTrue(result);
    }

    @Test
    void shouldReturnTrueIfRequiredFieldIsNumber() {
        Integer numberValue = 42;

        boolean result = Validator.validateRequiredField("testField", numberValue);
        assertTrue(result);
    }

    @Test
    void shouldReturnFalseIfRequiredFieldsAreMissing() {
        Map<String, Object> fields = new HashMap<>();
        fields.put("name", null);
        fields.put("email", "");

        boolean result = Validator.validateRequiredFields(fields);
        assertFalse(result);
    }

    @Test
    void shouldReturnFalseIfRequiredFieldsMapIsEmpty() {
        Map<String, Object> fields = new HashMap<>();

        boolean result = Validator.validateRequiredFields(fields);
        assertFalse(result);
    }

    @Test
    void shouldReturnFalseIfRequiredFieldsMapIsNull() {
        boolean result = Validator.validateRequiredFields(null);
        assertFalse(result);
    }

    @Test
    void shouldReturnTrueIfRequiredFieldsSuccessfully() {
        Map<String, Object> fields = new HashMap<>();
        fields.put("field1", "value1");
        fields.put("field2", "value2");

        boolean result = Validator.validateRequiredFields(fields);
        assertTrue(result);
    }

    @Test
    void shouldReturnTrueIfEmailFormatIsValid() {
        boolean result = Validator.validateEmailFormat("john.doe@example.com");
        assertTrue(result);
        result = Validator.validateEmailFormat("user123+any@subdomain.domain.co");
        assertTrue(result);
        result = Validator.validateEmailFormat("user.name@domain.org");
        assertTrue(result);
    }

    @Test
    void shouldReturnFalseIfEmailFormatIsInvalid() {
        boolean result = Validator.validateEmailFormat("invalid-email");
        assertFalse(result);
    }

    @Test
    void shouldReturnFalseIfEmailFormatIsMissingDomain() {
        boolean result = Validator.validateEmailFormat("username@.com");
        assertFalse(result);
    }

    @Test
    void shouldReturnFalseIfEmailFormatIsNull() {
        boolean result = Validator.validateEmailFormat(null);
        assertFalse(result);
    }

    @Test
    void shouldReturnFalseIfEmailFormatIsEmptyString() {
        boolean result = Validator.validateEmailFormat("");
        assertFalse(result);
    }

    @Test
    void shouldReturnTrueIfMinLengthIsValid() {
        boolean result = Validator.checkMinLength("ValidName", 3, "name");
        assertTrue(result);
    }

    @Test
    void shouldReturnFalseIfValueBelowMinLength() {
        boolean result = Validator.checkMinLength("ab", 3, "name");
        assertFalse(result);
    }

    @Test
    void shouldReturnFalseIfValueIsNullInCheckMinLength() {
        boolean result = Validator.checkMinLength(null, 3, "name");
        assertFalse(result);
    }
}
