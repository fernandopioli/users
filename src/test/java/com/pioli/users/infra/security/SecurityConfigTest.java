package com.pioli.users.infra.security;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.pioli.users.application.interfaces.PasswordHasher;

public class SecurityConfigTest {
    @Test
    void shouldCreatePasswordHasherBean() {
        SecurityConfig config = new SecurityConfig();

        PasswordHasher hasher = config.passwordHasher();

        assertNotNull(hasher);
        assertTrue(hasher instanceof BCryptPasswordHasher);
    }
}
