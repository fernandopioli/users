package com.pioli.users.application.usecases;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.pioli.users.application.interfaces.PasswordHasher;
import com.pioli.users.application.interfaces.UserRepository;
import com.pioli.users.domain.aggregate.User;
import com.pioli.users.domain.exceptions.AlreadyExistsException;
import com.pioli.users.domain.exceptions.ValidationException;
import com.pioli.users.domain.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

public class UpdateUserUseCaseTest {

    private UpdateUserUseCase updateUserUseCase;
    private UserRepository userRepository;
    private PasswordHasher passwordHasher;

    private UUID userId;
    private User existingUser;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        passwordHasher = mock(PasswordHasher.class);
        updateUserUseCase = new UpdateUserUseCase(userRepository, passwordHasher);

        userId = UUID.randomUUID();
        existingUser = User.load(
                userId,
                "Existing Name",
                "existing@example.com",
                "existingHashedPassword",
                null,
                null,
                null);
    }

    @Test
    void shouldUpdateUserSuccessfully() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.existsByEmailAndIdNot("newemail@example.com", userId)).thenReturn(false);
        when(passwordHasher.hash("newPassword")).thenReturn("newHashedPassword");

        User updatedUser = updateUserUseCase.execute(
                userId,
                "New Name",
                "newemail@example.com",
                "newPassword");

        assertNotNull(updatedUser);
        assertEquals("New Name", updatedUser.getName().getValue());
        assertEquals("newemail@example.com", updatedUser.getEmail().getValue());
        assertEquals("newHashedPassword", updatedUser.getPassword().getValue());

        verify(userRepository, times(1)).save(updatedUser);
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            updateUserUseCase.execute(userId, "New Name", "newemail@example.com", "newPassword");
        });

        assertEquals("User not found with id: " + userId, exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void shouldThrowExceptionWhenEmailAlreadyExists() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.existsByEmailAndIdNot("duplicate@example.com", userId)).thenReturn(true);

        AlreadyExistsException exception = assertThrows(AlreadyExistsException.class, () -> {
            updateUserUseCase.execute(userId, null, "duplicate@example.com", null);
        });

        assertEquals("Email already exists", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void shouldUpdateOnlyProvidedFields() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));

        User updatedUser = updateUserUseCase.execute(userId, "New Name", null, null);

        assertEquals("New Name", updatedUser.getName().getValue());
        assertEquals("existing@example.com", updatedUser.getEmail().getValue());
        assertEquals("existingHashedPassword", updatedUser.getPassword().getValue());

        verify(userRepository, times(1)).save(updatedUser);
    }

    @Test
    void shouldValidatePasswordBeforeHashing() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));

        ValidationException exception = assertThrows(ValidationException.class, () -> {
            updateUserUseCase.execute(userId, null, null, "123");
        });

        assertEquals("Field 'password' must have at least 6 characters", exception.getMessage());
        verify(passwordHasher, never()).hash(anyString());
        verify(userRepository, never()).save(any(User.class));
    }
}