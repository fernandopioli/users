package com.pioli.users.application.configuration;

import com.pioli.users.application.interfaces.DomainEventPublisher;
import com.pioli.users.application.interfaces.PasswordHasher;
import com.pioli.users.application.interfaces.UserRepository;
import com.pioli.users.application.usecases.CreateUserUseCase;
import com.pioli.users.application.usecases.DeleteUserUseCase;
import com.pioli.users.application.usecases.FindUserByIdUseCase;
import com.pioli.users.application.usecases.UpdateUserUseCase;
import com.pioli.users.application.usecases.ListAllUsersUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {

    private final UserRepository userRepository;
    private final PasswordHasher passwordHasher;
    private final DomainEventPublisher domainEventPublisher;

    public UseCaseConfig(UserRepository userRepository, PasswordHasher passwordHasher, DomainEventPublisher domainEventPublisher) {
        this.userRepository = userRepository;
        this.passwordHasher = passwordHasher;
        this.domainEventPublisher = domainEventPublisher;
    }

    @Bean
    public CreateUserUseCase createUserUseCase(UserRepository userRepository, PasswordHasher passwordHasher, DomainEventPublisher domainEventPublisher) {
        return new CreateUserUseCase(userRepository, passwordHasher, domainEventPublisher);
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
