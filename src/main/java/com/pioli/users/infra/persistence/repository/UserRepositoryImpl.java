package com.pioli.users.infra.persistence.repository;

import com.pioli.users.application.interfaces.UserRepository;
import com.pioli.users.domain.aggregate.User;
import com.pioli.users.domain.pagination.Page;
import com.pioli.users.domain.pagination.Pagination;
import com.pioli.users.infra.persistence.entity.UserEntity;
import com.pioli.users.infra.persistence.jpa.SpringDataUserRepository;

import jakarta.persistence.criteria.Predicate;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;


import java.util.*;
import java.util.stream.Collectors;

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
        return springDataUserRepository.findByIdAndDeletedAtIsNull(id)
                .map(this::toDomain);
    }

    @Override
    public boolean existsByEmailAndIdNot(String email, UUID id) {
        return springDataUserRepository.existsByEmailAndIdNot(email, id);
    }

    @Override
    public void delete(User user) {
        UserEntity userEntity = toEntity(user);
        springDataUserRepository.save(userEntity);
    }

    @Override
    public Page<User> findAll(Pagination pagination) {
        Pageable pageable = PageRequest.of(
            pagination.getPage(),
            pagination.getSize(),
            Sort.by(
                pagination.getSortDirection().equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC,
                pagination.getSortField()
            )
        );

        Specification<UserEntity> specification = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(criteriaBuilder.isNull(root.get("deletedAt")));

            for (Map.Entry<String, String> filter : pagination.getFilters().entrySet()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get(filter.getKey())), "%" + filter.getValue().toLowerCase() + "%"));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        org.springframework.data.domain.Page<UserEntity> userEntities =
            springDataUserRepository.findAll(specification, pageable);

        List<User> users = userEntities.stream()
            .map(this::toDomain)
            .collect(Collectors.toList());

        return new Page<>(
            users,
            userEntities.getNumber(),
            userEntities.getSize(),
            userEntities.getTotalElements(),
            userEntities.getTotalPages()
        );
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