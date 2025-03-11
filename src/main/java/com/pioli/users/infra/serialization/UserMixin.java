package com.pioli.users.infra.serialization;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonDeserialize(using = UserDeserializer.class)
public abstract class UserMixin {
    @JsonIgnore
    private Object password;

    @JsonIgnore
    private Object domainEvents;
}