package com.pioli.users.infra.serialization;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public abstract class UserMixin {
    @JsonIgnore
    private Object password;

    @JsonIgnore
    private Object domainEvents;
} 