package com.pioli.users.presentation.configuration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(GlobalExceptionHandler.class)
public class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void whenInvalidRoute_thenReturnsCustom404() throws Exception {
        mockMvc.perform(get("/v1/unexisting-route"))
               .andExpect(status().isNotFound())
               .andExpect(jsonPath("$.code").value(404))
               .andExpect(jsonPath("$.name").value("NOT_FOUND"))
               .andExpect(jsonPath("$.message").value("The requested page or resource was not found."));
    }
}