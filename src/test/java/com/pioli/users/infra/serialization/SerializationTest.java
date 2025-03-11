package com.pioli.users.infra.serialization;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.pioli.users.domain.aggregate.User;
import com.pioli.users.domain.events.UserCreatedEvent;
import com.pioli.users.domain.valueobject.Email;
import com.pioli.users.domain.valueobject.Name;
import com.pioli.users.domain.valueobject.Password;

public class SerializationTest {

    public static void main(String[] args) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.addMixIn(User.class, UserMixin.class);
        objectMapper.addMixIn(Name.class, NameMixin.class);
        objectMapper.addMixIn(Email.class, EmailMixin.class);

        Name name = Name.of("John Doe");
        Email email = Email.of("john.doe@example.com");
        Password password = Password.of("hashed_password");

        User user = User.create(name.getValue(), email.getValue(), password.getValue());
        UserCreatedEvent event = new UserCreatedEvent(user);

        String json = objectMapper.writeValueAsString(event);
        System.out.println("JSON Output: " + json);
    }
}