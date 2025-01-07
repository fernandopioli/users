package com.pioli.users.domain.validation;

import java.util.Map;

public final class Validator {
    private Validator() {}

    public static void validateRequired(Map<String, Object> fields) {
        if (fields == null || fields.isEmpty()) {
            throw new IllegalArgumentException("No fields provided for validation");
        }
        for (Map.Entry<String, Object> entry : fields.entrySet()) {
            String fieldName = entry.getKey();
            Object value = entry.getValue();

            if (value == null || (value instanceof String && ((String) value).trim().isEmpty())) {
                throw new IllegalArgumentException(
                    String.format("Field '%s' has an invalid value: '%s'", fieldName, value)
                );
            }
        }
    }
}
