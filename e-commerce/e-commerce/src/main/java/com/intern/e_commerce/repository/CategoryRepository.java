package com.intern.e_commerce.repository;

import com.intern.e_commerce.entity.Category;
import com.intern.e_commerce.entity.Role;
import com.intern.e_commerce.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findAll();
}
