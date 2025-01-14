package com.pioli.users.application.usecases;

import com.pioli.users.application.interfaces.UserRepository;
import com.pioli.users.domain.aggregate.User;
import com.pioli.users.domain.exceptions.ResourceNotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class FindUserByIdUseCaseTest {

    private UserRepository userRepository;
    private FindUserByIdUseCase findUserByIdUseCase;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        findUserByIdUseCase = new FindUserByIdUseCase(userRepository);
    }

    @Test
    void shouldReturnUserWhenFound() {
        UUID userId = UUID.randomUUID();
        User mockUser = User.create("Test User", "test@example.com", "hashedPassword");

        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));

        User result = findUserByIdUseCase.execute(userId);

        assertNotNull(result);
        assertEquals("Test User", result.getName().getValue());
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        UUID userId = UUID.randomUUID();

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            findUserByIdUseCase.execute(userId);
        });

        assertEquals("User not found with id: " + userId, exception.getMessage());
        verify(userRepository, times(1)).findById(userId);
    }
} 