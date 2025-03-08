package com.intern.e_commerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.intern.e_commerce.entity.Orders;

@Repository
public interface OrderRepository extends JpaRepository<Orders, Long> {}
