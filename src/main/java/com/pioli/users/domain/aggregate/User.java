package com.pioli.users.domain.aggregate;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.pioli.users.domain.base.Aggregate;
import com.pioli.users.domain.validation.Validator;

public class User extends Aggregate {
    private String name;
    private String email;
    private String password;

    private User(UUID id, String name, String email, String password, LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime deletedAt) {
        super(id, createdAt, updatedAt, deletedAt);
        this.name = name;
        this.email = email;
        this.password = password;
    }

    private User(String name, String email, String password) {
        super();
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public static User create(String name, String email, String password) {
        validateAllFields(name, email, password);
        return new User(name, email, password);
    }

    public static User load(UUID id, String name, String email, String password,
                            LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime deletedAt) {
        return new User(id, name, email, password, createdAt, updatedAt, deletedAt);
    }

    public void update(String name, String email, String password) {
        if (name != null) {
            validateName(name);
            this.name = name;
        }
        if (email != null) {
            validateEmail(email);
            this.email = email;
        }
        if (password != null) {
            validatePassword(password);
            this.password = password;
        }
        updateTimestamps();
    }

    public void delete() {
        markAsDeleted();
    }

    private static void validateName(String name) {
        Validator.validateRequiredField("name", name);
        Validator.checkMinLength(name, 3, "name");
    }

    private static void validateEmail(String email) {
        Validator.validateRequiredField("email", email);
        Validator.validateEmailFormat(email);
    }

    public static void validatePassword(String password) {
        Validator.validateRequiredField("password", password);
        Validator.checkMinLength(password, 6, "password");
    }

    private static void validateAllFields(String name, String email, String password) {
        validateName(name);
        validateEmail(email);
        validatePassword(password);
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
