package com.intern.e_commerce.repository;


import com.intern.e_commerce.entity.InvalidatedToken;
import com.intern.e_commerce.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Orders, Long> {}
