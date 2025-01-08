package com.pioli.users.application.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.pioli.users.application.interfaces.UserRepository;
import com.pioli.users.application.usecases.CreateUserUseCase;

@Configuration
public class UseCaseConfig {

    private final UserRepository userRepository;

    public UseCaseConfig(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Bean
    public CreateUserUseCase createUserUseCase() {
        return new CreateUserUseCase(userRepository);
    }
}
