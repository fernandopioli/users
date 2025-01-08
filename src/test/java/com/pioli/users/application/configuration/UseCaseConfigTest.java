package com.pioli.users.application.configuration;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.Test;

import com.pioli.users.application.interfaces.UserRepository;
import com.pioli.users.application.usecases.CreateUserUseCase;

public class UseCaseConfigTest {

    @Test
    void shouldCreateCreateUserUseCase() {
        UserRepository mockRepo = mock(UserRepository.class);

        UseCaseConfig config = new UseCaseConfig(mockRepo);

        CreateUserUseCase useCase = config.createUserUseCase();
        assertNotNull(useCase);
    }
}