package com.pioli.users.application.interfaces;

import com.pioli.users.domain.aggregate.User;

public interface UserRepository {
    boolean existsByEmail(String email);
    void save(User user);
}