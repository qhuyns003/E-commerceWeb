package com.intern.e_commerce.repository;

import com.intern.e_commerce.entity.Category;
import com.intern.e_commerce.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
}
