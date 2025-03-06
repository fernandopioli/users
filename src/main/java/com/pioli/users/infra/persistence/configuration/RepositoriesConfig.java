package com.pioli.users.infra.persistence.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.pioli.users.application.interfaces.UserRepository;
import com.pioli.users.infra.persistence.jpa.SpringDataUserRepository;
import com.pioli.users.infra.persistence.repository.UserRepositoryImpl;

@Configuration
public class RepositoriesConfig {

    @Bean
    public UserRepository userRepository(SpringDataUserRepository springDataUserRepository) {
        return new UserRepositoryImpl(springDataUserRepository);
    }
}