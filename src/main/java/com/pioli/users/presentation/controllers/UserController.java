package com.pioli.users.presentation.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.pioli.users.application.usecases.*;
import com.pioli.users.domain.aggregate.User;
import com.pioli.users.domain.pagination.Page;
import com.pioli.users.domain.pagination.Pagination;
import com.pioli.users.presentation.controllers.dtos.UserRequest;
import com.pioli.users.presentation.controllers.dtos.UserResponse;

import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.data.web.PagedResourcesAssembler;

@RestController
@RequestMapping("/users")
public class UserController {

    private final CreateUserUseCase createUserUseCase;
    private final UpdateUserUseCase updateUserUseCase;
    private final FindUserByIdUseCase findUserByIdUseCase;
    private final DeleteUserUseCase deleteUserUseCase;
    private final ListAllUsersUseCase listAllUsersUseCase;
    private final PagedResourcesAssembler<UserResponse> pagedResourcesAssembler;

    public UserController(CreateUserUseCase createUserUseCase,
                          UpdateUserUseCase updateUserUseCase,
                          FindUserByIdUseCase findUserByIdUseCase,
                          DeleteUserUseCase deleteUserUseCase,
                          ListAllUsersUseCase listAllUsersUseCase,
                          PagedResourcesAssembler<UserResponse> pagedResourcesAssembler) {
        this.createUserUseCase = createUserUseCase;
        this.updateUserUseCase = updateUserUseCase;
        this.findUserByIdUseCase = findUserByIdUseCase;
        this.deleteUserUseCase = deleteUserUseCase;
        this.listAllUsersUseCase = listAllUsersUseCase;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> findUserById(@PathVariable UUID id) {
        User user = findUserByIdUseCase.execute(id);
        UserResponse response = UserResponse.fromDomain(user);

        response.add(linkTo(methodOn(UserController.class).findUserById(id)).withSelfRel());
        response.add(linkTo(methodOn(UserController.class).listAllUsers(0, 10, "name", "asc", null)).withRel("users"));

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<UserResponse> createUser(@RequestBody UserRequest request) {
        User user = createUserUseCase.execute(request.getName(), request.getEmail(), request.getPassword());

        UserResponse response = UserResponse.fromDomain(user);

        response.add(linkTo(methodOn(UserController.class).findUserById(user.getId())).withSelfRel());
        response.add(linkTo(methodOn(UserController.class).listAllUsers(0, 10, "name", "asc", null)).withRel("users"));

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<EntityModel<UserResponse>> updateUser(
            @PathVariable UUID id,
            @RequestBody UserRequest request) {

        User updatedUser = updateUserUseCase.execute(
            id,
            request.getName(),
            request.getEmail(),
            request.getPassword()
        );

        UserResponse response = UserResponse.fromDomain(updatedUser);

        EntityModel<UserResponse> resource = EntityModel.of(response);
        resource.add(linkTo(methodOn(UserController.class).findUserById(id)).withSelfRel());
        resource.add(linkTo(methodOn(UserController.class).listAllUsers(0, 10, "name", "asc", null)).withRel("users"));

        return ResponseEntity.ok(resource);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID id) {
        deleteUserUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<PagedModel<EntityModel<UserResponse>>> listAllUsers(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(defaultValue = "name") String sortField,
        @RequestParam(defaultValue = "asc") String sortDirection,
        @RequestParam Map<String, String> filters
    ) {
        filters.keySet().removeAll(Arrays.asList("page", "size", "sortField", "sortDirection"));

        Map<String, String> allowedFilters = filters.entrySet().stream()
            .filter(entry -> entry.getKey().equalsIgnoreCase("name") || entry.getKey().equalsIgnoreCase("email"))
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        Pagination pagination = new Pagination(page, size, sortField, sortDirection, allowedFilters);

        Page<User> userPage = listAllUsersUseCase.execute(pagination);

        List<UserResponse> userResponses = userPage.getContent().stream()
            .map(UserResponse::fromDomain)
            .collect(Collectors.toList());

        org.springframework.data.domain.Page<UserResponse> springPage =
            new org.springframework.data.domain.PageImpl<>(
                userResponses,
                org.springframework.data.domain.PageRequest.of(userPage.getPage(), userPage.getSize()),
                userPage.getTotalElements()
            );

        PagedModel<EntityModel<UserResponse>> pagedModel = pagedResourcesAssembler.toModel(
            springPage,
            user -> {
                EntityModel<UserResponse> userModel = EntityModel.of(user);
                userModel.add(WebMvcLinkBuilder.linkTo(
                    WebMvcLinkBuilder.methodOn(UserController.class).findUserById(user.getId())).withSelfRel());
                return userModel;
            }
        );

        return ResponseEntity.ok(pagedModel);
    }
}
