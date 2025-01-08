package com.pioli.users.infra.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.pioli.users.application.interfaces.PasswordHasher;

@Configuration
public class SecurityConfig {
    
    @Bean
    public PasswordHasher passwordHasher() {
        return new BCryptPasswordHasher();
    }
}