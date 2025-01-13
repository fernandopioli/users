package com.pioli.users.application.usecases;

import com.pioli.users.application.interfaces.UserRepository;
import com.pioli.users.domain.aggregate.User;
import com.pioli.users.domain.exceptions.ResourceNotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class DeleteUserUseCaseTest {

    private UserRepository userRepository;
    private DeleteUserUseCase deleteUserUseCase;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        deleteUserUseCase = new DeleteUserUseCase(userRepository);
    }

    @Test
    void shouldMarkUserAsDeletedWithoutUpdatingUpdatedAt() {
        UUID userId = UUID.randomUUID();
        User user = User.create("Test User", "test@example.com", "password");
        LocalDateTime originalUpdatedAt = user.getUpdatedAt();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        deleteUserUseCase.execute(userId);

        assertNotNull(user.getDeletedAt());
        assertEquals(originalUpdatedAt, user.getUpdatedAt());
        verify(userRepository, times(1)).delete(user);
    }

    @Test
    void shouldThrowExceptionWhenUserDoesNotExist() {
        UUID userId = UUID.randomUUID();

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> deleteUserUseCase.execute(userId));

        assertEquals("User not found with id: " + userId, exception.getMessage());
        verify(userRepository, never()).delete(any(User.class));
    }
} 