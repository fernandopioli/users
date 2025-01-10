package com.pioli.users.domain.base;

import java.time.LocalDateTime;
import java.util.UUID;

public abstract class Aggregate extends Entity {
    protected Aggregate() {
        super();
    }

    protected Aggregate(UUID id, LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime deletedAt) {
        super(id, createdAt, updatedAt, deletedAt);
    }
}
