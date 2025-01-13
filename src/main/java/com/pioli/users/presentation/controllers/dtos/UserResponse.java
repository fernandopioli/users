package com.pioli.users.presentation.controllers.dtos;

import java.util.UUID;

public class UserResponse {
    private UUID id;
    private String name;
    private String email;

    public UserResponse(UUID id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }
}
