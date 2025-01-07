package com.pioli.users.application.usecases;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.pioli.users.application.interfaces.UserRepository;
import com.pioli.users.domain.aggregate.User;

public class CreateUserUseCaseTest {
    
    private CreateUserUseCase createUserUseCase;
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        createUserUseCase = new CreateUserUseCase(userRepository);
    }

    @Test
    void shouldCreateUserSuccessfully() {
        String name = "Any Name";
        String email = "mail@anymail.com";
        String password = "anyPassword";

        when(userRepository.existsByEmail(email)).thenReturn(false);

        User createdUser = createUserUseCase.execute(name, email, password);

        assertNotNull(createdUser);
        assertEquals(name, createdUser.getName());
        assertEquals(email, createdUser.getEmail());
        assertEquals(password, createdUser.getPassword());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void shouldThrowExceptionWhenEmailAlreadyExists() {
        String email = "existing.email@anymail.com";

        when(userRepository.existsByEmail(email)).thenReturn(true);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            createUserUseCase.execute("Any Name", email, "anyPassword");
        });

        assertEquals("Email already exists", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }
}
