package com.pioli.users.application.usecases;

import com.pioli.users.application.interfaces.PasswordHasher;
import com.pioli.users.application.interfaces.UserRepository;
import com.pioli.users.domain.aggregate.User;
import com.pioli.users.domain.exceptions.AlreadyExistsException;

public class CreateUserUseCase {
    private final UserRepository userRepository;
    private final PasswordHasher passwordHasher;

    public CreateUserUseCase(UserRepository userRepository, PasswordHasher passwordHasher) {
        this.userRepository = userRepository;
        this.passwordHasher = passwordHasher;
    }

    public User execute(String name, String email, String password) {
        if (userRepository.existsByEmail(email)) {
            throw new AlreadyExistsException("Email already exists");
        }

        String hashedPassword = passwordHasher.hash(password);
System.err.println(hashedPassword);
        User user = User.create(name, email, hashedPassword);
        userRepository.save(user);
        return user;
    }
}
