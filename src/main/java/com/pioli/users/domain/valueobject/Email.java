package com.pioli.users.domain.valueobject;

import com.pioli.users.domain.base.ValueObject;
import com.pioli.users.domain.validation.Validator;

public class Email extends ValueObject<String> {

    private Email(String value) {
        super(value);
    }

    public static Email of(String value) {
        validateEmail(value);
        return new Email(value.toLowerCase());
    }

    private static void validateEmail(String email) {
        Validator.validateRequiredField("email", email);
        Validator.validateEmailFormat(email);
    }
} 