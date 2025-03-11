package com.pioli.users.domain.valueobject;

import com.pioli.users.domain.base.ValueObject;
import com.pioli.users.domain.validation.Validator;
import com.pioli.users.domain.exceptions.ValidationException;
import com.pioli.users.domain.validation.ValidationResult;

public class Password extends ValueObject<String> {

    private Password(String value) {
        super(value);
    }

    public static Password of(String value) {
        validatePassword(value);
        return new Password(value);
    }

    public static void validatePassword(String password) {
        ValidationResult result = new ValidationResult();
        if (!Validator.validateRequiredField("password", password)) {
            result.addError("Password is required");
        }
        if (!Validator.checkMinLength(password, 6, "password")) {
            result.addError("Password must be at least 6 characters long");
        }
        if (!result.isValid()) {
            throw new ValidationException(String.join(", ", result.getErrors()));
        }
    }
} 