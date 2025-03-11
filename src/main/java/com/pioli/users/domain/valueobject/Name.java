package com.pioli.users.domain.valueobject;

import com.pioli.users.domain.base.ValueObject;
import com.pioli.users.domain.validation.Validator;
import com.pioli.users.domain.exceptions.ValidationException;
import com.pioli.users.domain.validation.ValidationResult;

public class Name extends ValueObject<String> {

    private Name(String value) {
        super(value);
        
    }

    public static Name of(String value) {
        validateName(value);
        return new Name(value);
    }

    private static void validateName(String name) {
        ValidationResult result = new ValidationResult();
        if (!Validator.validateRequiredField("name", name)) {
            result.addError("Name is required");
        }
        if (!Validator.checkMinLength(name, 3, "name")) {
            result.addError("Name must be at least 3 characters long");
        }
        if (!result.isValid()) {
            throw new ValidationException(String.join(", ", result.getErrors()));
        }
    }
} 