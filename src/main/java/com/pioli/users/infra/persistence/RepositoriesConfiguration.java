package com.pioli.users.infra.persistence;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.pioli.users.application.interfaces.UserRepository;
import com.pioli.users.infra.persistence.jpa.SpringDataUserRepository;
import com.pioli.users.infra.persistence.repository.UserRepositoryImpl;

@Configuration
public class RepositoriesConfiguration {

    @Bean
    public UserRepository userRepository(SpringDataUserRepository springDataUserRepository) {
        return new UserRepositoryImpl(springDataUserRepository);
    }
}