package com.pioli.users.presentation.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.pioli.users.application.usecases.CreateUserUseCase;
import com.pioli.users.application.usecases.FindUserByIdUseCase;
import com.pioli.users.application.usecases.UpdateUserUseCase;
import com.pioli.users.application.usecases.DeleteUserUseCase;
import com.pioli.users.domain.aggregate.User;
import com.pioli.users.presentation.controllers.dtos.UserRequest;
import com.pioli.users.presentation.controllers.dtos.UserResponse;

import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {

    private final CreateUserUseCase createUserUseCase;
    private final UpdateUserUseCase updateUserUseCase;
    private final FindUserByIdUseCase findUserByIdUseCase;
    private final DeleteUserUseCase deleteUserUseCase;

    public UserController(CreateUserUseCase createUserUseCase,
                          UpdateUserUseCase updateUserUseCase,
                          FindUserByIdUseCase findUserByIdUseCase,
                          DeleteUserUseCase deleteUserUseCase) {
        this.createUserUseCase = createUserUseCase;
        this.updateUserUseCase = updateUserUseCase;
        this.findUserByIdUseCase = findUserByIdUseCase;
        this.deleteUserUseCase = deleteUserUseCase;
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable UUID id) {
        User user = findUserByIdUseCase.execute(id);
        UserResponse response = new UserResponse(
            user.getId(),
            user.getName(),
            user.getEmail());
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<UserResponse> createUser(@RequestBody UserRequest request) {
        User user = createUserUseCase.execute(request.getName(), request.getEmail(), request.getPassword());

        UserResponse response = new UserResponse(
            user.getId(),
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
            updatedUser.getId(),
            updatedUser.getName(),
            updatedUser.getEmail()
        );

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID id) {
        deleteUserUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }
}
