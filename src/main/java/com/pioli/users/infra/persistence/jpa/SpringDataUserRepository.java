package com.pioli.users.infra.persistence.jpa;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pioli.users.infra.persistence.entity.UserEntity;


@Repository
public interface SpringDataUserRepository extends JpaRepository<UserEntity, UUID> {
    
    boolean existsByEmail(String email);
    boolean existsByEmailAndIdNot(String email, UUID id);
}
