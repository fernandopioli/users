package com.pioli.users.application.usecases;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.pioli.users.application.interfaces.DomainEventPublisher;
import com.pioli.users.application.interfaces.PasswordHasher;
import com.pioli.users.application.interfaces.UserRepository;
import com.pioli.users.domain.aggregate.User;
import com.pioli.users.domain.exceptions.AlreadyExistsException;
import com.pioli.users.domain.exceptions.InvalidParameterException;

public class CreateUserUseCaseTest {
    
    private CreateUserUseCase createUserUseCase;
    private UserRepository userRepository;
    private PasswordHasher passwordHasher;
    private DomainEventPublisher domainEventPublisher;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        passwordHasher = mock(PasswordHasher.class);
        domainEventPublisher = mock(DomainEventPublisher.class);
        createUserUseCase = new CreateUserUseCase(userRepository, passwordHasher, domainEventPublisher);
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
        assertEquals(name, createdUser.getName().getValue());
        assertEquals(email, createdUser.getEmail().getValue());
        assertEquals("hashedPassword", createdUser.getPassword().getValue());
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
        verify(passwordHasher, never()).hash(anyString());
    }

    @Test
    void shouldThrowExceptionWhenPasswordIsInvalid() {
        String password = "123";

        when(userRepository.existsByEmail("any@mail.com")).thenReturn(false);

        InvalidParameterException exception = assertThrows(InvalidParameterException.class, () -> {
            createUserUseCase.execute("Any Name", "any@mail.com", password);
        });

        assertEquals("Field 'password' must have at least 6 characters", exception.getMessage());
        verify(passwordHasher, never()).hash(anyString());
        verify(userRepository, never()).save(any(User.class));
    }
}
