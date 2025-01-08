package com.pioli.users.infra.security;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class BCryptPasswordHasherTest {

    private BCryptPasswordHasher hasher;

    @BeforeEach
    void setUp() {
        hasher = new BCryptPasswordHasher();
    }

    @Test
    void shouldHashPasswordAndNotReturnPlainText() {
        String plain = "secret123";
        String hashed = hasher.hash(plain);

        assertNotEquals(plain, hashed);
        assertNotNull(hashed);
        assertFalse(hashed.isEmpty());
    }

    @Test
    void shouldMatchValidPassword() {
        String plain = "password";
        String hashed = hasher.hash(plain);

        assertTrue(hasher.matches(plain, hashed));
    }

    @Test
    void shouldNotMatchInvalidPassword() {
        String plain = "password";
        String hashed = hasher.hash(plain);

        assertFalse(hasher.matches("different", hashed));
    }
    
}
