package com.pioli.users.infra.persistence;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import com.pioli.users.application.interfaces.UserRepository;
import com.pioli.users.infra.persistence.repository.UserRepositoryImpl;

@Import(RepositoriesConfiguration.class)
@DataJpaTest
class RepositoriesConfigurationTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void shouldCreateUserRepositoryBean() {
        assertNotNull(userRepository);
        assertTrue(userRepository instanceof UserRepositoryImpl);
    }
}