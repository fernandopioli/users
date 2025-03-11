package com.pioli.users.domain.valueobject;

import com.pioli.users.domain.base.ValueObject;
import com.pioli.users.domain.validation.Validator;
import com.pioli.users.domain.exceptions.ValidationException;

public class Name extends ValueObject<String> {

    private Name(String value) {
        super(value);

    }

    public static Name of(String value) {
        validateName(value);
        return new Name(value);
    }

    private static void validateName(String name) {
        if (!Validator.validateRequiredField("name", name)) {
            throw new ValidationException("Field 'name' is required and cannot be empty");
        }
        if (!Validator.checkMinLength(name, 3, "name")) {
            throw new ValidationException("Field 'name' must have at least 3 characters");
        }
    }
}