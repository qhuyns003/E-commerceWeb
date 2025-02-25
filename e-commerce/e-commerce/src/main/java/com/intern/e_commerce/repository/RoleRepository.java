package com.intern.e_commerce.repository;

import com.intern.e_commerce.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<com.intern.e_commerce.entity.Role, String> {}
