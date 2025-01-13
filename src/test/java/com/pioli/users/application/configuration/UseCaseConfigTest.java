package com.pioli.users.application.configuration;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.Test;

import com.pioli.users.application.interfaces.PasswordHasher;
import com.pioli.users.application.interfaces.UserRepository;
import com.pioli.users.application.usecases.CreateUserUseCase;
import com.pioli.users.application.usecases.DeleteUserUseCase;
import com.pioli.users.application.usecases.UpdateUserUseCase;
import com.pioli.users.application.usecases.FindUserByIdUseCase;

public class UseCaseConfigTest {

    @Test
    void shouldCreateCreateUserUseCase() {
        UserRepository mockRepo = mock(UserRepository.class);
        PasswordHasher mockHash = mock(PasswordHasher.class);

        UseCaseConfig config = new UseCaseConfig(mockRepo, mockHash);

        CreateUserUseCase createUseCase = config.createUserUseCase();
        assertNotNull(createUseCase);

        UpdateUserUseCase updateUseCase = config.updateUserUseCase();
        assertNotNull(updateUseCase);

        FindUserByIdUseCase findByIdUseCase = config.findUserByIdUseCase();
        assertNotNull(findByIdUseCase);

        DeleteUserUseCase deleteUseCase = config.deleteUserUseCase();
        assertNotNull(deleteUseCase);
    }
}
