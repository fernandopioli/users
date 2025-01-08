package com.pioli.users.application.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.pioli.users.application.interfaces.PasswordHasher;
import com.pioli.users.application.interfaces.UserRepository;
import com.pioli.users.application.usecases.CreateUserUseCase;

@Configuration
public class UseCaseConfig {

    private final UserRepository userRepository;
    private final PasswordHasher passwordHasher;

    public UseCaseConfig(UserRepository userRepository, PasswordHasher passwordHasher) {
        this.userRepository = userRepository;
        this.passwordHasher = passwordHasher;
    }

    @Bean
    public CreateUserUseCase createUserUseCase() {
        return new CreateUserUseCase(userRepository, passwordHasher);
    }
}
