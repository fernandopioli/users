package com.pioli.users.application.usecases;

import com.pioli.users.application.interfaces.UserRepository;
import com.pioli.users.domain.aggregate.User;

public class CreateUserUseCase {
    private final UserRepository userRepository;

    public CreateUserUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User execute(String name, String email, String password) {
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email already exists");
        }

        User user = User.create(name, email, password);
        userRepository.save(user);
        return user;
    }
}
