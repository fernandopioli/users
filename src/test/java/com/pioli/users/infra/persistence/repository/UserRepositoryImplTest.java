package com.pioli.users.infra.persistence.repository;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.pioli.users.application.interfaces.UserRepository;
import com.pioli.users.domain.aggregate.User;
import com.pioli.users.infra.persistence.jpa.SpringDataUserRepository;

@DataJpaTest
class UserRepositoryImplTest {

    @Autowired
    private SpringDataUserRepository springDataUserRepository;

    private UserRepository userRepository;

    @BeforeEach
    void setup() {
        userRepository = new UserRepositoryImpl(springDataUserRepository);
    }

    @Test
    void shouldPersistUser() {
        User user = User.create("Test Name", "test@mail.com", "pass123");

        userRepository.save(user);

        boolean exists = userRepository.existsByEmail("test@mail.com");
        assertTrue(exists);
    }
}