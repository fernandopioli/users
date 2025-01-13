package com.pioli.users.application.usecases;

import com.pioli.users.application.interfaces.UserRepository;
import com.pioli.users.domain.aggregate.User;
import com.pioli.users.domain.exceptions.ResourceNotFoundException;

import java.util.UUID;

public class DeleteUserUseCase {

    private final UserRepository userRepository;

    public DeleteUserUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void execute(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        user.delete();
        userRepository.delete(user);
    }
} 