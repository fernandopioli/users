package com.pioli.users.infra.serialization;

import com.fasterxml.jackson.annotation.JsonValue;

public abstract class NameMixin {

    @JsonValue
    public abstract String getValue();
} 