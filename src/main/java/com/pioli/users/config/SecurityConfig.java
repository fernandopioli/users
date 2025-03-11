package com.pioli.users.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.pioli.users.application.interfaces.PasswordHasher;
import com.pioli.users.infra.security.BCryptPasswordHasher;

@Configuration
public class SecurityConfig {
    
    @Bean
    public PasswordHasher passwordHasher() {
        return new BCryptPasswordHasher();
    }
}