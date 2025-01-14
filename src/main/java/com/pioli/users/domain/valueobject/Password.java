package com.pioli.users.domain.valueobject;

import com.pioli.users.domain.base.ValueObject;
import com.pioli.users.domain.validation.Validator;

public class Password extends ValueObject<String> {

    private Password(String value) {
        super(value);
    }

    public static Password of(String value) {
        validatePassword(value);
        return new Password(value);
    }

    public static void validatePassword(String password) {
        Validator.validateRequiredField("password", password);
        Validator.checkMinLength(password, 6, "password");
    }
} 