package com.pioli.users.infra.serialization;

import com.fasterxml.jackson.annotation.JsonValue;

public abstract class EmailMixin {

    @JsonValue
    public abstract String getValue();
} 