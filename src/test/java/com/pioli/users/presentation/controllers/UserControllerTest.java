package com.pioli.users.presentation.controllers;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Collections;
import org.mockito.ArgumentCaptor;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pioli.users.application.usecases.CreateUserUseCase;
import com.pioli.users.application.usecases.DeleteUserUseCase;
import com.pioli.users.application.usecases.FindUserByIdUseCase;
import com.pioli.users.application.usecases.ListAllUsersUseCase;
import com.pioli.users.application.usecases.UpdateUserUseCase;
import com.pioli.users.domain.aggregate.User;
import com.pioli.users.domain.exceptions.AlreadyExistsException;
import com.pioli.users.domain.exceptions.InvalidParameterException;
import com.pioli.users.domain.exceptions.RequiredParameterException;
import com.pioli.users.domain.exceptions.ResourceNotFoundException;
import com.pioli.users.domain.pagination.Page;
import com.pioli.users.domain.pagination.Pagination;
import com.pioli.users.presentation.controllers.dtos.UserRequest;
import com.pioli.users.presentation.configuration.GlobalExceptionHandler;
import org.springframework.context.annotation.Import;

@WebMvcTest(UserController.class)
@Import(GlobalExceptionHandler.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CreateUserUseCase createUserUseCase;

    @MockBean
    private UpdateUserUseCase updateUserUseCase;

    @MockBean
    private FindUserByIdUseCase findUserByIdUseCase;

    @MockBean
    private DeleteUserUseCase deleteUserUseCase;

    @MockBean
    private ListAllUsersUseCase listAllUsersUseCase;

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
            LocalDateTime.now(),
            LocalDateTime.now(),
            null
        );
    }

    @Test
    void shouldCreateUserAndReturn201() throws Exception {

        UserRequest request = new UserRequest();
        request.setName("Any Name");
        request.setEmail("mail@example.com");
        request.setPassword("123456");

        User mockUser = User.create("Any Name", "mail@example.com", "123456");
        when(createUserUseCase.execute(eq("Any Name"), eq("mail@example.com"), eq("123456")))
                .thenReturn(mockUser);


                mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(mockUser.getId().toString()))
                .andExpect(jsonPath("$.name").value(mockUser.getName().getValue()))
                .andExpect(jsonPath("$.email").value(mockUser.getEmail().getValue()));
                verify(createUserUseCase, times(1)).execute(eq("Any Name"), eq("mail@example.com"), eq("123456"));

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

        mockMvc.perform(patch("/users/" + userId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(userId.toString()))
            .andExpect(jsonPath("$.name").value(existingUser.getName().getValue()))
            .andExpect(jsonPath("$.email").value(existingUser.getEmail().getValue()));
            verify(updateUserUseCase, times(1)).execute(eq(userId), eq("New Name"), eq("newemail@example.com"), eq("newPassword"));
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

        mockMvc.perform(patch("/users/" + userId)
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

        mockMvc.perform(patch("/users/" + userId)
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

        mockMvc.perform(patch("/users/" + userId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isConflict())
            .andExpect(jsonPath("$.code").value(409))
            .andExpect(jsonPath("$.name").value("ALREADY_EXISTS"))
            .andExpect(jsonPath("$.message").value("Email already exists"));
    }

    @Test
    void shouldReturnUserWhenGetById() throws Exception {
        UUID userId = UUID.randomUUID();
        User mockUser = User.load(userId, "Test User", "test@example.com", "hashedPassword", null, null, null);

        when(findUserByIdUseCase.execute(userId)).thenReturn(mockUser);

        mockMvc.perform(get("/users/" + userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId.toString()))
                .andExpect(jsonPath("$.name").value("Test User"))
                .andExpect(jsonPath("$.email").value("test@example.com"));
                verify(findUserByIdUseCase, times(1)).execute(eq(userId));
    }

    @Test
    void shouldReturnNotFoundWhenUserDoesNotExistOnFindById() throws Exception {
        UUID userId = UUID.randomUUID();

        when(findUserByIdUseCase.execute(userId)).thenThrow(new ResourceNotFoundException("User not found with id: " + userId));

        mockMvc.perform(get("/users/" + userId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(404))
                .andExpect(jsonPath("$.name").value("RESOURCE_NOT_FOUND"))
                .andExpect(jsonPath("$.message").value("User not found with id: " + userId));
    }

    @Test
    void shouldDeleteUserSuccessfully() throws Exception {
        UUID userId = UUID.randomUUID();

        doNothing().when(deleteUserUseCase).execute(userId);

        mockMvc.perform(delete("/users/" + userId))
                .andExpect(status().isNoContent());

        verify(deleteUserUseCase, times(1)).execute(userId);
    }

    @Test
    void shouldReturnNotFoundWhenDeletingNonExistingUser() throws Exception {
        UUID userId = UUID.randomUUID();

        doThrow(new ResourceNotFoundException("User not found with id: " + userId))
                .when(deleteUserUseCase).execute(userId);

        mockMvc.perform(delete("/users/" + userId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(404))
                .andExpect(jsonPath("$.name").value("RESOURCE_NOT_FOUND"))
                .andExpect(jsonPath("$.message").value("User not found with id: " + userId));

        verify(deleteUserUseCase, times(1)).execute(userId);
    }

    @Test
    void shouldReturnBadRequestForInvalidUUID() throws Exception {
        String invalidUUID = "invalid-uuid";

        mockMvc.perform(get("/users/" + invalidUUID))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.name").value("INVALID_UUID"))
                .andExpect(jsonPath("$.message").value("The provided ID is not a valid UUID: " + invalidUUID));
    }

    @Test
    void shouldListAllUsersSuccessfully() throws Exception {
        List<User> users = Arrays.asList(
                User.load(UUID.randomUUID(), "User One", "user1@example.com", "hashedPassword", LocalDateTime.now(), LocalDateTime.now(), null),
                User.load(UUID.randomUUID(), "User Two", "user2@example.com", "hashedPassword", LocalDateTime.now(), LocalDateTime.now(), null)
        );

        Page<User> userPage = new Page<>(users, 0, 2, 2, 1);

        when(listAllUsersUseCase.execute(any(Pagination.class))).thenReturn(userPage);

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.users[0].name").value("User One"))
                .andExpect(jsonPath("$._embedded.users[1].name").value("User Two"));

        verify(listAllUsersUseCase, times(1)).execute(any(Pagination.class));
    }

    @Test
    void shouldListUsersWithFiltersSuccessfully() throws Exception {
        List<User> users = Arrays.asList(
                User.load(UUID.randomUUID(), "User One", "user1@example.com", "hashedPassword", LocalDateTime.now(), LocalDateTime.now(), null),
                User.load(UUID.randomUUID(), "User Two", "user2@example.com", "hashedPassword", LocalDateTime.now(), LocalDateTime.now(), null)
        );

        Page<User> userPage = new Page<>(users, 0, 1, 1, 1);

        when(listAllUsersUseCase.execute(any(Pagination.class))).thenReturn(userPage);

        mockMvc.perform(get("/users")
                .param("name", "one"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.users[0].name").value("User One"));

        verify(listAllUsersUseCase, times(1)).execute(any(Pagination.class));
    }

    @Test
    void shouldApplyOnlyValidFiltersAndIgnoreInvalidOnes() throws Exception {
        // Arrange
        UUID userId = UUID.randomUUID();
        User user = User.load(
            userId,
            "Alice",
            "alice@example.com",
            "hashedPassword",
            LocalDateTime.now(),
            LocalDateTime.now(),
            null
        );

        List<User> users = Collections.singletonList(user);
        Page<User> userPage = new Page<>(users, 0, 10, 1, 1);

        when(listAllUsersUseCase.execute(any(Pagination.class))).thenReturn(userPage);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("name", "Alice");
        params.add("age", "30");
        params.add("email", "alice@example.com");
        params.add("unknown", "value");

        mockMvc.perform(get("/users").params(params))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$._embedded.users[0].name").value("Alice"))
            .andExpect(jsonPath("$._embedded.users[0].email").value("alice@example.com"));

        ArgumentCaptor<Pagination> paginationCaptor = ArgumentCaptor.forClass(Pagination.class);
        verify(listAllUsersUseCase, times(1)).execute(paginationCaptor.capture());

        Pagination capturedPagination = paginationCaptor.getValue();

        assertTrue(capturedPagination.getFilters().containsKey("name"));
        assertTrue(capturedPagination.getFilters().containsKey("email"));
        assertFalse(capturedPagination.getFilters().containsKey("age"));
        assertFalse(capturedPagination.getFilters().containsKey("unknown"));
    }

    @Test
    void whenHttpMethodNotSupported_thenHandleMethodNotSupported() throws Exception {
        mockMvc.perform(put("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(jsonPath("$.code").value(405))
                .andExpect(jsonPath("$.name").value("METHOD_NOT_ALLOWED"));
    }
}
