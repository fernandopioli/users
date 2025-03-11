package com.pioli.users.config;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.Test;

import com.pioli.users.application.interfaces.DomainEventPublisher;
import com.pioli.users.application.interfaces.PasswordHasher;
import com.pioli.users.application.interfaces.UserRepository;
import com.pioli.users.application.usecases.CreateUserUseCase;
import com.pioli.users.application.usecases.DeleteUserUseCase;
import com.pioli.users.application.usecases.UpdateUserUseCase;
import com.pioli.users.application.usecases.FindUserByIdUseCase;
import com.pioli.users.application.usecases.ListAllUsersUseCase;

public class UseCaseConfigTest {

    @Test
    void shouldCreateCreateUserUseCase() {
        UserRepository mockRepo = mock(UserRepository.class);
        PasswordHasher mockHash = mock(PasswordHasher.class);
        DomainEventPublisher mockPublisher = mock(DomainEventPublisher.class);

        UseCaseConfig config = new UseCaseConfig(mockRepo, mockHash, mockPublisher);

        CreateUserUseCase createUseCase = config.createUserUseCase(mockRepo, mockHash, mockPublisher);
        assertNotNull(createUseCase);

        UpdateUserUseCase updateUseCase = config.updateUserUseCase();
        assertNotNull(updateUseCase);

        FindUserByIdUseCase findByIdUseCase = config.findUserByIdUseCase();
        assertNotNull(findByIdUseCase);

        DeleteUserUseCase deleteUseCase = config.deleteUserUseCase();
        assertNotNull(deleteUseCase);

        ListAllUsersUseCase listAllUseCase = config.listAllUsersUseCase();
        assertNotNull(listAllUseCase);
    }
}
