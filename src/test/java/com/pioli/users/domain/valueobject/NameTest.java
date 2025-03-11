package com.pioli.users.domain.valueobject;

import com.pioli.users.domain.exceptions.ValidationException;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class NameTest {

    @Test
    void shouldCreateValidName() {
        Name name = Name.of("Valid Name");
        assertEquals("Valid Name", name.getValue());
    }

    @Test
    void shouldThrowExceptionForNullName() {
        Exception exception = assertThrows(ValidationException.class, () -> {
            Name.of(null);
        });
        assertEquals("Field 'name' is required and cannot be empty", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionForShortName() {
        Exception exception = assertThrows(ValidationException.class, () -> {
            Name.of("ab");
        });
        assertEquals("Field 'name' must have at least 3 characters", exception.getMessage());
    }

    @Test
    void shouldTestEquality() {
        Name name1 = Name.of("Name");
        Name name2 = Name.of("Name");
        Name name3 = Name.of("Different");

        assertEquals(name1, name2);
        assertNotEquals(name1, name3);
    }
}