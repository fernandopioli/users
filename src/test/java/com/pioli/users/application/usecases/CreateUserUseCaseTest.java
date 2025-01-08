package com.pioli.users.application.usecases;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.pioli.users.application.interfaces.PasswordHasher;
import com.pioli.users.application.interfaces.UserRepository;
import com.pioli.users.domain.aggregate.User;
import com.pioli.users.domain.exceptions.AlreadyExistsException;

public class CreateUserUseCaseTest {
    
    private CreateUserUseCase createUserUseCase;
    private UserRepository userRepository;
    private PasswordHasher passwordHasher;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        passwordHasher = mock(PasswordHasher.class);
        createUserUseCase = new CreateUserUseCase(userRepository, passwordHasher);
    }

    @Test
    void shouldCreateUserSuccessfully() {
        String name = "Any Name";
        String email = "mail@anymail.com";
        String password = "anyPassword";

        when(userRepository.existsByEmail(email)).thenReturn(false);
        when(passwordHasher.hash("anyPassword")).thenReturn("hashedPassword");

        User createdUser = createUserUseCase.execute(name, email, password);

        assertNotNull(createdUser);
        assertEquals(name, createdUser.getName());
        assertEquals(email, createdUser.getEmail());
        assertEquals("hashedPassword", createdUser.getPassword());
        verify(passwordHasher, times(1)).hash("anyPassword");
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void shouldThrowExceptionWhenEmailAlreadyExists() {
        String email = "existing.email@anymail.com";

        when(userRepository.existsByEmail(email)).thenReturn(true);

        AlreadyExistsException exception = assertThrows(AlreadyExistsException.class, () -> {
            createUserUseCase.execute("Any Name", email, "anyPassword");
        });

        assertEquals("Email already exists", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }
}
