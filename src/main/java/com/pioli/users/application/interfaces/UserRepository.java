package com.pioli.users.application.interfaces;

import java.util.Optional;
import java.util.UUID;

import com.pioli.users.domain.aggregate.User;

public interface UserRepository {
    boolean existsByEmail(String email);
    void save(User user);
    void delete(User user);
    Optional<User> findById(UUID id);
    boolean existsByEmailAndIdNot(String email, UUID id);
}