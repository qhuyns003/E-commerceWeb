package com.intern.e_commerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.intern.e_commerce.entity.Permission;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, String> {
    public Permission findByName(String name);
}
