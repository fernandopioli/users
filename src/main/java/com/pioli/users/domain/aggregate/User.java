package com.pioli.users.domain.aggregate;

import com.pioli.users.domain.base.Aggregate;
import com.pioli.users.domain.events.UserCreatedEvent;
import com.pioli.users.domain.valueobject.Email;
import com.pioli.users.domain.valueobject.Name;
import com.pioli.users.domain.valueobject.Password;

import java.time.LocalDateTime;
import java.util.UUID;

public class User extends Aggregate {
    private Name name;
    private Email email;
    private Password password;

    private User(UUID id, Name name, Email email, Password password, LocalDateTime createdAt, LocalDateTime updatedAt,
            LocalDateTime deletedAt) {
        super(id, createdAt, updatedAt, deletedAt);
        this.name = name;
        this.email = email;
        this.password = password;
    }

    private User(Name name, Email email, Password password) {
        super();
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public static User create(String name, String email, String password) {
        Name nameObj = Name.of(name);
        Email emailObj = Email.of(email);
        Password passwordObj = Password.of(password);
        User user = new User(nameObj, emailObj, passwordObj);
        user.recordEvent(new UserCreatedEvent(user));
        return user;
    }

    public static User load(UUID id, String name, String email, String password,
            LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime deletedAt) {
        Name nameObj = Name.of(name);
        Email emailObj = Email.of(email);
        Password passwordObj = password != null ? Password.of(password) : null;
        return new User(id, nameObj, emailObj, passwordObj, createdAt, updatedAt, deletedAt);
    }

    private void updateName(String name) {
        Name nameObj = Name.of(name);
        this.name = nameObj;
    }

    private void updateEmail(String email) {
        Email emailObj = Email.of(email);
        this.email = emailObj;
    }

    private void updatePassword(String password) {
        Password passwordObj = Password.of(password);
        this.password = passwordObj;
    }

    public void update(String name, String email, String password) {
        boolean updated = false;
        if (name != null) {
            updateName(name);
            updated = true;
        }
        if (email != null) {
            updateEmail(email);
            updated = true;
        }
        if (password != null) {
            updatePassword(password);
            updated = true;
        }
        if (updated) {
            updateTimestamps();
        }
    }

    public void delete() {
        markAsDeleted();
    }

    public Name getName() {
        return name;
    }

    public Email getEmail() {
        return email;
    }

    public Password getPassword() {
        return password;
    }
}
