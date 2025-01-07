package com.pioli.users.infra.persistence.repository;

import com.pioli.users.application.interfaces.UserRepository;
import com.pioli.users.domain.aggregate.User;
import com.pioli.users.infra.persistence.entity.UserEntity;
import com.pioli.users.infra.persistence.jpa.SpringDataUserRepository;

public class UserRepositoryImpl implements UserRepository {
    private final SpringDataUserRepository springDataUserRepository;

    public UserRepositoryImpl(SpringDataUserRepository springDataUserRepository) {
        this.springDataUserRepository = springDataUserRepository;
    }

    @Override
    public boolean existsByEmail(String email) {
        return springDataUserRepository.existsByEmail(email);
    }

@Override
    public void save(User user) {
        UserEntity userEntity = toEntity(user);
        springDataUserRepository.save(userEntity);
    }

    private UserEntity toEntity(User user) {
        UserEntity entity = new UserEntity(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getPassword(),
                user.getCreatedAt(),
                user.getUpdatedAt(),
                user.getDeletedAt()
        );

        return entity;
    }
}