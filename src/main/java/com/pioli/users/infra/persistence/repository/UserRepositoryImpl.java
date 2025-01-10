package com.pioli.users.infra.persistence.repository;

import java.util.Optional;
import java.util.UUID;

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

    @Override
    public Optional<User> findById(UUID id) {
        return springDataUserRepository.findById(id)
                .map(this::toDomain);
    }

    @Override
    public boolean existsByEmailAndIdNot(String email, UUID id) {
        return springDataUserRepository.existsByEmailAndIdNot(email, id);
    }

    private UserEntity toEntity(User user) {
        return new UserEntity(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getPassword(),
                user.getCreatedAt(),
                user.getUpdatedAt(),
                user.getDeletedAt()
        );
    }

    private User toDomain(UserEntity entity) {
        return User.load(
            entity.getId(),
            entity.getName(),
            entity.getEmail(),
            entity.getPassword(),
            entity.getCreatedAt(),
            entity.getUpdatedAt(),
            entity.getDeletedAt()
        );
    }
}