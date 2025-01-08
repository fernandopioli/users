package com.pioli.users.presentation.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.pioli.users.application.usecases.CreateUserUseCase;
import com.pioli.users.domain.aggregate.User;
import com.pioli.users.presentation.controllers.dtos.CreateUserRequest;
import com.pioli.users.presentation.controllers.dtos.UserResponse;

@RestController
@RequestMapping("/users")
public class UserController {

    private final CreateUserUseCase createUserUseCase;

    public UserController(CreateUserUseCase createUserUseCase) {
        this.createUserUseCase = createUserUseCase;
    }

    @PostMapping
    public ResponseEntity<UserResponse> createUser(@RequestBody CreateUserRequest request) {
        User user = createUserUseCase.execute(request.getName(), request.getEmail(), request.getPassword());

        UserResponse response = new UserResponse(
            user.getId().toString(),
            user.getName(),
            user.getEmail()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
}
