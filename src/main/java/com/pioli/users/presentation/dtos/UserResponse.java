package com.pioli.users.presentation.dtos;

import com.pioli.users.domain.aggregate.User;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.time.LocalDateTime;
import java.util.UUID;

@Relation(collectionRelation = "users")
public class UserResponse extends RepresentationModel<UserResponse> {
    private UUID id;
    private String name;
    private String email;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public UserResponse(UUID id, String name, String email, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static UserResponse fromDomain(User user) {
        return new UserResponse(user.getId(), user.getName().getValue(), user.getEmail().getValue(), user.getCreatedAt(), user.getUpdatedAt());
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
