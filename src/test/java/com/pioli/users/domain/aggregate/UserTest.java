package com.pioli.users.domain.aggregate;

import java.time.LocalDateTime;
import com.pioli.users.domain.base.Aggregate;
import com.pioli.users.domain.exceptions.InvalidParameterException;
import com.pioli.users.domain.exceptions.RequiredParameterException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import org.mockito.Mockito;
import static org.mockito.Mockito.times;
import org.mockito.MockedStatic;

public class UserTest {

    private User user;

    private static final String VALID_NAME = "Name Lastname";
    private static final String VALID_EMAIL = "name@example.com";
    private static final String VALID_PASSWORD = "password123";

    private static final String INVALID_EMAIL = "invalid_email";
    private static final String SHORT_PASSWORD = "12345";
    
    @BeforeEach
    void setUp() {
        user = User.create(VALID_NAME, VALID_EMAIL, VALID_PASSWORD);
    }

    @Test
    void shouldCreateUserWithValidData() {
        assertTrue(user instanceof Aggregate);
        assertNotNull(user.getId());
        assertEquals(VALID_NAME, user.getName());
        assertEquals(VALID_EMAIL, user.getEmail());
        assertEquals(VALID_PASSWORD, user.getPassword());
    }

    @Test
    void shouldThrowExceptionForRequiredParams() {
        RequiredParameterException exceptionName = assertThrows(RequiredParameterException.class, () -> {
            User.create("", VALID_EMAIL, VALID_PASSWORD);
        });

        assertEquals("Field 'name' has an invalid value: ''", exceptionName.getMessage());

        RequiredParameterException exceptionEmail = assertThrows(RequiredParameterException.class, () -> {
            User.create(VALID_NAME, "", VALID_PASSWORD);
        });

        assertEquals("Field 'email' has an invalid value: ''", exceptionEmail.getMessage());

        RequiredParameterException exceptionPassword = assertThrows(RequiredParameterException.class, () -> {
            User.create(VALID_NAME, VALID_EMAIL, "");
        });

        assertEquals("Field 'password' has an invalid value: ''", exceptionPassword.getMessage());
    }

    @Test
    void shouldThrowExceptionForInvalidEmail() {
        InvalidParameterException exception = assertThrows(InvalidParameterException.class, () -> {
            User.create(VALID_NAME, INVALID_EMAIL, VALID_PASSWORD);
        });

        assertEquals("Invalid email format", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionForShortPassword() {
        InvalidParameterException exception = assertThrows(InvalidParameterException.class, () -> {
            User.create(VALID_NAME, VALID_EMAIL, SHORT_PASSWORD);
        });

        assertEquals("Field 'password' must have at least 6 characters", exception.getMessage());
    }

    @Test
    void shouldUpdateUserNameAndEmail() {
        user.update("Another Name", "anotheremail@example.com", "password456");

        assertEquals("Another Name", user.getName());
        assertEquals("anotheremail@example.com", user.getEmail());
        assertEquals("password456", user.getPassword());
    }

    @Test
    void shouldCallValidateWhenUpdateIsInvoked() {
        try (MockedStatic<User> mockedStatic = Mockito.mockStatic(User.class)) {
            user.update("Another Name", "anotheremail@example.com", "password456");

            mockedStatic.verify(() -> User.validate("Another Name", "anotheremail@example.com", "password456"), times(1));
        }
    }

    @Test
    void shouldDeleteUser() {
        user.delete();

        assertTrue(user.isDeleted());
        assertTrue(user.getDeletedAt() instanceof LocalDateTime);
        assertNotNull(user.getDeletedAt());
    }

}
