package com.pioli.users.infra.serialization;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.pioli.users.domain.valueobject.Email;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EmailMixinTest {

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        @SuppressWarnings("unused")
        SimpleModule module = new SimpleModule();
        objectMapper.addMixIn(Email.class, EmailMixin.class);
    }

    @Test
    void shouldSerializeEmailAsString() throws Exception {
        Email email = Email.of("test@example.com");

        String json = objectMapper.writeValueAsString(email);

        assertThat(json).isEqualTo("\"test@example.com\"");
    }

    @Test
    void shouldDeserializeStringToEmail() throws Exception {
        String json = "\"test@example.com\"";

        Email email = objectMapper.readValue(json, Email.class);

        assertThat(email.getValue()).isEqualTo("test@example.com");
    }
}