package com.intern.e_commerce.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.intern.e_commerce.entity.UserEntity;

@Repository
@Transactional
public interface UserRepositoryInterface extends JpaRepository<UserEntity, String> {
    List<UserEntity> findAll();

    boolean existsByUsername(String username);

    Optional<UserEntity> findByUsername(String username);
}
