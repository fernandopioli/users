package com.pioli.users.domain.validation;

import java.util.Map;
import java.util.regex.Pattern;

import com.pioli.users.domain.exceptions.InvalidParameterException;
import com.pioli.users.domain.exceptions.RequiredParameterException;

public final class Validator {
    private Validator() {}

    private static final Pattern EMAIL_REGEX = Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");


    public static void validateRequiredFields(Map<String, Object> fields) {
        if (fields == null || fields.isEmpty()) {
            throw new RequiredParameterException("No fields provided for validation");
        }
        for (Map.Entry<String, Object> entry : fields.entrySet()) {
            validateRequiredField(entry.getKey(), entry.getValue());
        }
    }

    public static void validateRequiredField(String fieldName, Object value) {
        if (value == null || (value instanceof String && ((String) value).trim().isEmpty())) {
            throw new RequiredParameterException(
                String.format("Field '%s' is required and cannot be empty", fieldName)
            );
        }
    }

    public static void validateEmailFormat(String email) {
        if (email == null || !EMAIL_REGEX.matcher(email).matches()) {
            throw new InvalidParameterException("Invalid email format");
        }
    }

    public static void checkMinLength(String value, int minLength, String fieldName) {
        if (value == null || value.length() < minLength) {
            throw new InvalidParameterException(
                String.format("Field '%s' must have at least %d characters", fieldName, minLength)
            );
        }
    }
}
