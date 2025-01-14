package com.pioli.users.infra.persistence.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Optional;
import java.util.UUID;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.pioli.users.application.interfaces.UserRepository;
import com.pioli.users.domain.aggregate.User;
import com.pioli.users.domain.pagination.Page;
import com.pioli.users.domain.pagination.Pagination;
import com.pioli.users.infra.persistence.jpa.SpringDataUserRepository;

@DataJpaTest
class UserRepositoryImplTest {

    @Autowired
    private SpringDataUserRepository springDataUserRepository;

    private UserRepository userRepository;

    @BeforeEach
    void setup() {
        userRepository = new UserRepositoryImpl(springDataUserRepository);
    }

    @Test
    void shouldPersistUser() {
        User user = User.create("Test Name", "test@mail.com", "pass123");

        userRepository.save(user);

        Optional<User> storedUser = userRepository.findById(user.getId());
        assertTrue(storedUser.isPresent());
        assertEquals("Test Name", storedUser.get().getName());
        assertEquals("test@mail.com", storedUser.get().getEmail());
    }

    @Test
    void shouldFindUserById() {
        User user = User.create("Test User", "test@example.com", "hashedPassword");
        userRepository.save(user);

        Optional<User> foundUser = userRepository.findById(user.getId());
        assertTrue(foundUser.isPresent());
        assertEquals("Test User", foundUser.get().getName());
    }

    @Test
    void shouldReturnTrueWhenEmailExistsAndIdIsDifferent() {
        User user1 = User.create("User One", "email@example.com", "password1");
        userRepository.save(user1);

        User user2 = User.create("User Two", "email2@example.com", "password2");
        userRepository.save(user2);

        boolean exists = userRepository.existsByEmailAndIdNot("email@example.com", user2.getId());

        assertTrue(exists);
    }

    @Test
    void shouldReturnFalseWhenEmailExistsAndIdIsSame() {
        User user = User.create("Test User", "email@example.com", "password");
        userRepository.save(user);

        boolean exists = userRepository.existsByEmailAndIdNot("email@example.com", user.getId());

        assertFalse(exists);
    }

    @Test
    void shouldReturnFalseWhenEmailDoesNotExist() {
        User user = User.create("Test User", "email@example.com", "password");
        userRepository.save(user);

        boolean exists = userRepository.existsByEmailAndIdNot("nonexistent@example.com", UUID.randomUUID());

        assertFalse(exists);
    }

    @Test
    void shouldUpdateUser() {
        User user = User.create("Original Name", "original@example.com", "password");
        userRepository.save(user);

        user.update("Updated Name", null, null);
        userRepository.save(user);

        Optional<User> updatedUser = userRepository.findById(user.getId());
        assertTrue(updatedUser.isPresent());
        assertEquals("Updated Name", updatedUser.get().getName());
    }

    @Test
    void shouldNotFindDeletedUser() {
        User user = User.create("Test Name", "test@mail.com", "pass123");
        userRepository.save(user);

        user.delete();
        userRepository.save(user);

        Optional<User> storedUser = userRepository.findById(user.getId());
        assertFalse(storedUser.isPresent());
    }

    @Test
    void shouldReturnTrueIfEmailExists() {
        User user = User.create("Test User", "existing@example.com", "password123");
        userRepository.save(user);

        boolean exists = userRepository.existsByEmail("existing@example.com");
        assertTrue(exists);
    }

    @Test
    void shouldReturnFalseIfEmailDoesNotExist() {
        boolean exists = userRepository.existsByEmail("nonexistent@example.com");
        assertFalse(exists);
    }

    @Test
    void shouldSoftDeleteUser() {
        User user = User.create("User To Delete", "userToDelete@example.com", "password123");
        userRepository.save(user);

        assertTrue(userRepository.findById(user.getId()).isPresent());

        user.delete();

        userRepository.delete(user);

        assertFalse(userRepository.findById(user.getId()).isPresent());

        var userEntityOptional = springDataUserRepository.findById(user.getId());
        assertTrue(userEntityOptional.isPresent());
        assertNotNull(userEntityOptional.get().getDeletedAt());
    }

    @Test
    void shouldFindAllUsersWithFilters() {
        User user1 = User.create("Bob", "bob@example.com", "hashedPassword");
        User user2 = User.create("Alice", "alice@example.com", "hashedPassword");

        userRepository.save(user1);
        userRepository.save(user2);

        Map<String, String> filters = new HashMap<>();
        filters.put("name", "Alice");

        Pagination pagination = new Pagination(0, 10, "name", "asc", filters);

        Page<User> result = userRepository.findAll(pagination);

        assertEquals(1, result.getContent().size());
        assertEquals("Alice", result.getContent().get(0).getName());
    }

    @Test
    void shouldSortUsersInDescendingOrder() {
        User user1 = User.create("Bob", "bob@example.com", "hashedPassword");
        User user2 = User.create("Alice", "alice@example.com", "hashedPassword");

        userRepository.save(user1);
        userRepository.save(user2);


        Pagination pagination = new Pagination(0, 10, "name", "desc", new HashMap<>());

        Page<User> result = userRepository.findAll(pagination);

        assertEquals(2, result.getContent().size());
        assertEquals("Bob", result.getContent().get(0).getName());
    }
}
