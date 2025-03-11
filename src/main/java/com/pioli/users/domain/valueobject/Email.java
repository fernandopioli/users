package com.pioli.users.domain.valueobject;

import com.pioli.users.domain.base.ValueObject;
import com.pioli.users.domain.exceptions.ValidationException;
import com.pioli.users.domain.validation.Validator;
import com.pioli.users.domain.validation.ValidationResult;
public class Email extends ValueObject<String> {

    private Email(String value) {
        super(value);
    }

    public static Email of(String value) {
        validateEmail(value);
        return new Email(value.toLowerCase());
    }

    private static void validateEmail(String email) {
        ValidationResult result = new ValidationResult();
        if (!Validator.validateRequiredField("email", email)) {
            result.addError("Email is required");
        }
        if (!Validator.validateEmailFormat(email)) {
            result.addError("Invalid email format");
        }
        if (!result.isValid()) {
            throw new ValidationException(String.join(", ", result.getErrors()));
        }
    }
} 