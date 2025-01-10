package com.pioli.users.application.usecases;

import java.util.Optional;
import java.util.UUID;

import com.pioli.users.application.interfaces.PasswordHasher;
import com.pioli.users.application.interfaces.UserRepository;
import com.pioli.users.domain.aggregate.User;
import com.pioli.users.domain.exceptions.AlreadyExistsException;
import com.pioli.users.domain.exceptions.ResourceNotFoundException;

public class UpdateUserUseCase {
    private final UserRepository userRepository;
    private final PasswordHasher passwordHasher;

    public UpdateUserUseCase(UserRepository userRepository, PasswordHasher passwordHasher) {
        this.userRepository = userRepository;
        this.passwordHasher = passwordHasher;
    }

    public User execute(UUID id, String name, String email, String password) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (!optionalUser.isPresent()) {
            throw new ResourceNotFoundException("User not found with id: " + id);
        }

        User user = optionalUser.get();

        if (email != null && userRepository.existsByEmailAndIdNot(email, id)) {
            throw new AlreadyExistsException("Email already exists");
        }

        String hashedPassword = null;
        if (password != null) {
            User.validatePassword(password);
            hashedPassword = passwordHasher.hash(password);
        }

        user.update(name, email, hashedPassword);

        userRepository.save(user);
        return user;
    }
} 