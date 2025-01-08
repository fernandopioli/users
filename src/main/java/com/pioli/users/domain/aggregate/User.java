package com.pioli.users.domain.aggregate;

import java.util.HashMap;
import java.util.Map;

import com.pioli.users.domain.base.Aggregate;
import com.pioli.users.domain.exceptions.InvalidParameterException;
import com.pioli.users.domain.validation.Validator;

public class User extends Aggregate {
    private String name;
    private String email;
    private String password;

    private User(String name, String email, String password) {
        super();
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public static User create(String name, String email, String password) {
        validate(name, email, password);
        return new User(name, email, password);
    }

    public void update(String name, String email, String password) {
        User.validate(name, email, password);
        this.name = name;
        this.email = email;
        this.password = password;
        updateTimestamps();
    }

    public void delete() {
        markAsDeleted();
    }

    protected static void validate(String name, String email, String password) {
        Map<String, Object> fields = new HashMap<>() {{
            put("name", name);
            put("email", email);
            put("password", password);
        }};

        Validator.validateRequired(fields);
        Validator.validateEmailFormat(email);
        Validator.checkMinLength(password, 6, "password");
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
    
}
