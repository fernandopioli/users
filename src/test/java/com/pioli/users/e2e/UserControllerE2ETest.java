package com.pioli.users.e2e;

import com.pioli.users.UsersApplication;
import com.pioli.users.infra.persistence.jpa.SpringDataUserRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = UsersApplication.class)
@AutoConfigureMockMvc
public class UserControllerE2ETest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SpringDataUserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    void shouldCreateAndRetrieveUserSuccessfully() throws Exception {
        String userJson = """
            {
                "name": "John Doe",
                "email": "john.doe@example.com",
                "password": "password123"
            }
            """;

        MvcResult result = mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"))
                .andExpect(jsonPath("$._links.self.href").exists())
                .andReturn();

        String responseContent = result.getResponse().getContentAsString();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode responseJson = objectMapper.readTree(responseContent);
        String userId = responseJson.get("id").asText();

        mockMvc.perform(get("/users/" + userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId))
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"))
                .andExpect(jsonPath("$._links.self.href").exists());
    }

    @Test
    void shouldUpdateUserSuccessfully() throws Exception {
        String userJson = """
            {
                "name": "Jane Doe",
                "email": "jane.doe@example.com",
                "password": "password123"
            }
            """;

        MvcResult result = mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson))
                .andExpect(status().isCreated())
                .andReturn();

        String responseContent = result.getResponse().getContentAsString();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode responseJson = objectMapper.readTree(responseContent);
        String userId = responseJson.get("id").asText();

        String updateJson = """
            {
                "name": "Jane Smith",
                "email": "jane.smith@example.com",
                "password": "newpassword123"
            }
            """;

        mockMvc.perform(patch("/users/" + userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId))
                .andExpect(jsonPath("$.name").value("Jane Smith"))
                .andExpect(jsonPath("$.email").value("jane.smith@example.com"))
                .andExpect(jsonPath("$._links.self.href").exists());

        mockMvc.perform(get("/users/" + userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId))
                .andExpect(jsonPath("$.name").value("Jane Smith"))
                .andExpect(jsonPath("$.email").value("jane.smith@example.com"));
    }

    @Test
    void shouldDeleteUserSuccessfully() throws Exception {
        String userJson = """
            {
                "name": "Bob Brown",
                "email": "bob.brown@example.com",
                "password": "password123"
            }
            """;

        MvcResult result = mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson))
                .andExpect(status().isCreated())
                .andReturn();

        String responseContent = result.getResponse().getContentAsString();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode responseJson = objectMapper.readTree(responseContent);
        String userId = responseJson.get("id").asText();

        mockMvc.perform(delete("/users/" + userId))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/users/" + userId))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldListAllUsersSuccessfully() throws Exception {
        String userJson1 = """
            {
                "name": "Alice",
                "email": "alice@example.com",
                "password": "password123"
            }
            """;
        String userJson2 = """
            {
                "name": "Bob",
                "email": "bob@example.com",
                "password": "password123"
            }
            """;

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson1))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson2))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.users").isArray())
                .andExpect(jsonPath("$._embedded.users.length()").value(2))
                .andExpect(jsonPath("$._embedded.users[?(@.name == 'Alice')]").exists())
                .andExpect(jsonPath("$._embedded.users[?(@.name == 'Bob')]").exists());
    }
} 