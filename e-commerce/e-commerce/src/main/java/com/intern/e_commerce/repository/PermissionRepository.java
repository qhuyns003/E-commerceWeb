package com.intern.e_commerce.repository;


import com.intern.e_commerce.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, String> {
    public Permission findByName(String name);
}
