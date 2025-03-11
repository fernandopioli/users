package com.pioli.users.domain.validation;

import java.util.Map;
import java.util.regex.Pattern;

public final class Validator {
    private Validator() {
    }

    private static final Pattern EMAIL_REGEX = Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    public static boolean validateRequiredField(String fieldName, Object value) {
        if (value == null || (value instanceof String && ((String) value).trim().isEmpty())) {
            return false;
        }
        return true;
    }

    public static boolean validateRequiredFields(Map<String, Object> fields) {
        if (fields == null || fields.isEmpty()) {
            return false;
        }
        for (Map.Entry<String, Object> entry : fields.entrySet()) {
            if (!validateRequiredField(entry.getKey(), entry.getValue())) {
                return false;
            }
        }
        return true;
    }

    public static boolean validateEmailFormat(String email) {
        if (email == null || !EMAIL_REGEX.matcher(email).matches()) {
            return false;
        }
        return true;
    }

    public static boolean checkMinLength(String value, int minLength, String fieldName) {
        if (value == null || value.length() < minLength) {
            return false;
        }
        return true;
    }
}
