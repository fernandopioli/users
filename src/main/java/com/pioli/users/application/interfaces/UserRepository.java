package com.pioli.users.application.interfaces;

import java.util.Optional;
import java.util.UUID;

import com.pioli.users.domain.aggregate.User;
import com.pioli.users.domain.pagination.Page;
import com.pioli.users.domain.pagination.Pagination;

public interface UserRepository {
    boolean existsByEmail(String email);
    void save(User user);
    void delete(User user);
    Optional<User> findById(UUID id);
    boolean existsByEmailAndIdNot(String email, UUID id);
    Page<User> findAll(Pagination pagination);
}