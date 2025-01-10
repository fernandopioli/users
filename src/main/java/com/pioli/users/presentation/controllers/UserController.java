package com.pioli.users.presentation.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.pioli.users.application.usecases.CreateUserUseCase;
import com.pioli.users.application.usecases.UpdateUserUseCase;
import com.pioli.users.domain.aggregate.User;
import com.pioli.users.presentation.controllers.dtos.UserRequest;
import com.pioli.users.presentation.controllers.dtos.UserResponse;

import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {

    private final CreateUserUseCase createUserUseCase;
    private final UpdateUserUseCase updateUserUseCase;

    public UserController(CreateUserUseCase createUserUseCase, UpdateUserUseCase updateUserUseCase) {
        this.createUserUseCase = createUserUseCase;
        this.updateUserUseCase = updateUserUseCase;
    }

    @PostMapping
    public ResponseEntity<UserResponse> createUser(@RequestBody UserRequest request) {
        User user = createUserUseCase.execute(request.getName(), request.getEmail(), request.getPassword());

        UserResponse response = new UserResponse(
            user.getId().toString(),
            user.getName(),
            user.getEmail()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(
            @PathVariable UUID id,
            @RequestBody UserRequest request) {
        
        User updatedUser = updateUserUseCase.execute(
            id,
            request.getName(),
            request.getEmail(),
            request.getPassword()
        );

        UserResponse response = new UserResponse(
            updatedUser.getId().toString(),
            updatedUser.getName(),
            updatedUser.getEmail()
        );

        return ResponseEntity.ok(response);
    }
}
