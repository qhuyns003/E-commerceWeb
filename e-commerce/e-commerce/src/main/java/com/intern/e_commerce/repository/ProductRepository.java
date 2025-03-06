package com.intern.e_commerce.repository;

import com.intern.e_commerce.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    @Query(value = "SELECT * FROM product WHERE name LIKE %:keyword%", nativeQuery = true)
    List<Product> findProductByName(@Param("keyword") String keyword);
}
