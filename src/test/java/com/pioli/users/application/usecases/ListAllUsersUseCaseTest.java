package com.pioli.users.application.usecases;

import com.pioli.users.application.interfaces.UserRepository;
import com.pioli.users.domain.aggregate.User;
import com.pioli.users.domain.pagination.Page;
import com.pioli.users.domain.pagination.Pagination;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ListAllUsersUseCaseTest {

    private ListAllUsersUseCase listAllUsersUseCase;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        listAllUsersUseCase = new ListAllUsersUseCase(userRepository);
    }

    @Test
    void shouldListAllUsersSuccessfully() {
        User user1 = User.load(UUID.randomUUID(), "Alice", "alice@example.com", "hashedPassword", LocalDateTime.now(), LocalDateTime.now(), null);
        User user2 = User.load(UUID.randomUUID(), "Bob", "bob@example.com", "hashedPassword", LocalDateTime.now(), LocalDateTime.now(), null);

        List<User> users = Arrays.asList(user1, user2);
        Pagination pagination = new Pagination(0, 10, "name", "asc", new HashMap<>());

        Page<User> expectedPage = new Page<>(users, 0, 10, users.size(), 1);

        when(userRepository.findAll(pagination)).thenReturn(expectedPage);

        Page<User> resultPage = listAllUsersUseCase.execute(pagination);

        assertNotNull(resultPage);
        assertEquals(2, resultPage.getContent().size());
        assertEquals("Alice", resultPage.getContent().get(0).getName());
        assertEquals("Bob", resultPage.getContent().get(1).getName());
        verify(userRepository, times(1)).findAll(pagination);
    }

    @Test
    void shouldListUsersWithFiltersSuccessfully() {
        User user = User.load(UUID.randomUUID(), "Alice", "alice@example.com", "hashedPassword", LocalDateTime.now(), LocalDateTime.now(), null);

        Map<String, String> filters = new HashMap<>();
        filters.put("name", "Alice");

        Pagination pagination = new Pagination(0, 10, "name", "asc", filters);

        List<User> users = Collections.singletonList(user);
        Page<User> expectedPage = new Page<>(users, 0, 10, 1, 1);

        when(userRepository.findAll(pagination)).thenReturn(expectedPage);

        Page<User> resultPage = listAllUsersUseCase.execute(pagination);

        assertNotNull(resultPage);
        assertEquals(1, resultPage.getContent().size());
        assertEquals("Alice", resultPage.getContent().get(0).getName());
        verify(userRepository, times(1)).findAll(pagination);
    }
} 