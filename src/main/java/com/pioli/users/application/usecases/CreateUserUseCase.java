package com.pioli.users.application.usecases;

import com.pioli.users.application.interfaces.DomainEventPublisher;
import com.pioli.users.application.interfaces.PasswordHasher;
import com.pioli.users.application.interfaces.UserRepository;
import com.pioli.users.domain.aggregate.User;
import com.pioli.users.domain.exceptions.AlreadyExistsException;
import com.pioli.users.domain.valueobject.Password;
import com.pioli.users.domain.events.DomainEvent;

public class CreateUserUseCase {
    private final UserRepository userRepository;
    private final PasswordHasher passwordHasher;
    private final DomainEventPublisher domainEventPublisher;

    public CreateUserUseCase(UserRepository userRepository, PasswordHasher passwordHasher, DomainEventPublisher domainEventPublisher) {
        this.userRepository = userRepository;
        this.passwordHasher = passwordHasher;
        this.domainEventPublisher = domainEventPublisher;
    }

    public User execute(String name, String email, String password) {
        if (userRepository.existsByEmail(email)) {
            throw new AlreadyExistsException("Email already exists");
        }

        Password.validatePassword(password);

        String hashedPassword = passwordHasher.hash(password);

        User user = User.create(name, email, hashedPassword);
        userRepository.save(user);

        user.getDomainEvents().forEach(domainEvent -> domainEventPublisher.publish(user.getId().toString(), domainEvent));
        user.clearDomainEvents();

        return user;
    }
}
