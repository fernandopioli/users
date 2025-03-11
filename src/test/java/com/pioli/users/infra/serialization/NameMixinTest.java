package com.pioli.users.infra.serialization;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pioli.users.domain.valueobject.Name;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NameMixinTest {

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.addMixIn(Name.class, NameMixin.class);
    }

    @Test
    void shouldSerializeNameAsString() throws Exception {
        Name name = Name.of("John Doe");

        String json = objectMapper.writeValueAsString(name);

        assertThat(json).isEqualTo("\"John Doe\"");
    }

    @Test
    void shouldDeserializeStringToName() throws Exception {
        String json = "\"John Doe\"";

        Name name = objectMapper.readValue(json, Name.class);

        assertThat(name.getValue()).isEqualTo("John Doe");
    }
}