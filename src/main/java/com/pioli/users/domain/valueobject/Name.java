package com.pioli.users.domain.valueobject;

import com.pioli.users.domain.base.ValueObject;
import com.pioli.users.domain.validation.Validator;

public class Name extends ValueObject<String> {

    private Name(String value) {
        super(value);
        
    }

    public static Name of(String value) {
        validateName(value);
        return new Name(value);
    }

    private static void validateName(String name) {
        Validator.validateRequiredField("name", name);
        Validator.checkMinLength(name, 3, "name");
    }
} 