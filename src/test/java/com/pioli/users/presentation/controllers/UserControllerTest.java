package com.pioli.users.presentation.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pioli.users.application.usecases.CreateUserUseCase;
import com.pioli.users.application.usecases.UpdateUserUseCase;
import com.pioli.users.domain.aggregate.User;
import com.pioli.users.domain.exceptions.AlreadyExistsException;
import com.pioli.users.domain.exceptions.InvalidParameterException;
import com.pioli.users.domain.exceptions.RequiredParameterException;
import com.pioli.users.domain.exceptions.ResourceNotFoundException;
import com.pioli.users.presentation.controllers.dtos.UserRequest;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CreateUserUseCase createUserUseCase;

    @MockBean
    private UpdateUserUseCase updateUserUseCase;

    @Autowired
    private ObjectMapper objectMapper;

    private UUID userId;
    private User existingUser;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        existingUser = User.load(
            userId,
            "Existing Name",
            "existing@example.com",
            "existingHashedPassword",
            null,
            null,
            null
        );
    }

    @Test
    void shouldCreateUserAndReturn201() throws Exception {

        User mockUser = User.create("Any Name", "mail@example.com", "123456");
        when(createUserUseCase.execute(eq("Any Name"), eq("mail@example.com"), eq("123456")))
                .thenReturn(mockUser);


                mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                      "name": "Any Name",
                      "email": "mail@example.com",
                      "password": "123456"
                    }
                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(mockUser.getId().toString()))
                .andExpect(jsonPath("$.name").value(mockUser.getName()))
                .andExpect(jsonPath("$.email").value(mockUser.getEmail()));

    }

    @Test
    void shouldReturn400WhenRequiredParameterExceptionIsThrown() throws Exception {
        when(createUserUseCase.execute(any(), any(), any()))
            .thenThrow(new RequiredParameterException("Name is required"));

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                      "email": "john@example.com",
                      "password": "123456"
                    }
                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.name").value("REQUIRED_PARAM"))
                .andExpect(jsonPath("$.message").value("Name is required"));
    }

    @Test
    void shouldReturn400WhenInvalidParameterExceptionIsThrown() throws Exception {
        when(createUserUseCase.execute(anyString(), anyString(), anyString()))
            .thenThrow(new InvalidParameterException("Invalid email format"));

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                      "name": "Any Name",
                      "email": "invalidEmail",
                      "password": "123456"
                    }
                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.name").value("INVALID_PARAM"))
                .andExpect(jsonPath("$.message").value("Invalid email format"));
    }

    @Test
    void shouldReturn409WhenAlreadyExistsExceptionIsThrown() throws Exception {
        when(createUserUseCase.execute(anyString(), anyString(), anyString()))
            .thenThrow(new AlreadyExistsException("Email already exists"));

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                      "name": "Another Name",
                      "email": "existing.email@anymail.com",
                      "password": "123456"
                    }
                """))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.code").value(409))
                .andExpect(jsonPath("$.name").value("ALREADY_EXISTS"))
                .andExpect(jsonPath("$.message").value("Email already exists"));
    }

    @Test
    void shouldReturn500WhenUnexpectedExceptionIsThrown() throws Exception {
        when(createUserUseCase.execute(anyString(), anyString(), anyString()))
            .thenThrow(new RuntimeException("Unexpected"));

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                      "name": "Any Name",
                      "email": "any@mail.com",
                      "password": "123456"
                    }
                """))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.code").value(500))
                .andExpect(jsonPath("$.name").value("INTERNAL_ERROR"))
                .andExpect(jsonPath("$.message").value("An unexpected error occurred"));
    }

    @Test
    void shouldUpdateUserSuccessfully() throws Exception {
        UserRequest request = new UserRequest();
        request.setName("New Name");
        request.setEmail("newemail@example.com");
        request.setPassword("newPassword");

        when(updateUserUseCase.execute(
            eq(userId),
            eq("New Name"),
            eq("newemail@example.com"),
            eq("newPassword")
        )).thenReturn(existingUser);

        mockMvc.perform(put("/users/" + userId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(userId.toString()))
            .andExpect(jsonPath("$.name").value("Existing Name"))
            .andExpect(jsonPath("$.email").value("existing@example.com"));
    }

    @Test
    void shouldReturnNotFoundWhenUserDoesNotExist() throws Exception {
        UserRequest request = new UserRequest();
        request.setName("New Name");
        request.setEmail("newemail@example.com");
        request.setPassword("newPassword");

        when(updateUserUseCase.execute(
            eq(userId),
            anyString(),
            anyString(),
            anyString()
        )).thenThrow(new ResourceNotFoundException("User not found with id: " + userId));

        mockMvc.perform(put("/users/" + userId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.code").value(404))
            .andExpect(jsonPath("$.name").value("RESOURCE_NOT_FOUND"))
            .andExpect(jsonPath("$.message").value("User not found with id: " + userId));
    }

    @Test
    void shouldReturnBadRequestWhenInvalidDataProvided() throws Exception {
        UserRequest request = new UserRequest();
        request.setName("New Name");
        request.setEmail("newemail@example.com");
        request.setPassword("123");

        when(updateUserUseCase.execute(
            eq(userId),
            anyString(),
            anyString(),
            eq("123")
        )).thenThrow(new InvalidParameterException("Field 'password' must have at least 6 characters"));

        mockMvc.perform(put("/users/" + userId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.code").value(400))
            .andExpect(jsonPath("$.name").value("INVALID_PARAM"))
            .andExpect(jsonPath("$.message").value("Field 'password' must have at least 6 characters"));
    }

    @Test
    void shouldReturnConflictWhenEmailAlreadyExistsDuringUpdate() throws Exception {
        UserRequest request = new UserRequest();
        request.setName("New Name");
        request.setEmail("existing.email@example.com");
        request.setPassword("123");

        when(updateUserUseCase.execute(
            eq(userId),
            anyString(),
            eq("existing.email@example.com"),
            anyString()
        )).thenThrow(new AlreadyExistsException("Email already exists"));

        mockMvc.perform(put("/users/" + userId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isConflict())
            .andExpect(jsonPath("$.code").value(409))
            .andExpect(jsonPath("$.name").value("ALREADY_EXISTS"))
            .andExpect(jsonPath("$.message").value("Email already exists"));
    }
}
