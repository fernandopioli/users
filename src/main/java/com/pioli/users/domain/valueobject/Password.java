package com.pioli.users.domain.valueobject;

import com.pioli.users.domain.base.ValueObject;
import com.pioli.users.domain.validation.Validator;
import com.pioli.users.domain.exceptions.ValidationException;

public class Password extends ValueObject<String> {

    private Password(String value) {
        super(value);
    }

    public static Password of(String value) {
        validatePassword(value);
        return new Password(value);
    }

    public static void validatePassword(String password) {
        if (!Validator.validateRequiredField("password", password)) {
            throw new ValidationException("Field 'password' is required and cannot be empty");
        }
        if (!Validator.checkMinLength(password, 6, "password")) {
            throw new ValidationException("Field 'password' must have at least 6 characters");
        }
    }
}