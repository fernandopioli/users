package com.pioli.users.presentation.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.pioli.users.application.usecases.CreateUserUseCase;
import com.pioli.users.domain.aggregate.User;
import com.pioli.users.domain.exceptions.AlreadyExistsException;
import com.pioli.users.domain.exceptions.InvalidParameterException;
import com.pioli.users.domain.exceptions.RequiredParameterException;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CreateUserUseCase createUserUseCase;

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
}
