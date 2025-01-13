package com.pioli.users.application.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.pioli.users.application.interfaces.PasswordHasher;
import com.pioli.users.application.interfaces.UserRepository;
import com.pioli.users.application.usecases.CreateUserUseCase;
import com.pioli.users.application.usecases.DeleteUserUseCase;
import com.pioli.users.application.usecases.FindUserByIdUseCase;
import com.pioli.users.application.usecases.UpdateUserUseCase;
import com.pioli.users.application.usecases.ListAllUsersUseCase;

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

    @Bean
    public UpdateUserUseCase updateUserUseCase() {
        return new UpdateUserUseCase(userRepository, passwordHasher);
    }

    @Bean
    public FindUserByIdUseCase findUserByIdUseCase() {
        return new FindUserByIdUseCase(userRepository);
    }

    @Bean
    public DeleteUserUseCase deleteUserUseCase() {
        return new DeleteUserUseCase(userRepository);
    }

    @Bean
    public ListAllUsersUseCase listAllUsersUseCase() {
        return new ListAllUsersUseCase(userRepository);
    }
}
