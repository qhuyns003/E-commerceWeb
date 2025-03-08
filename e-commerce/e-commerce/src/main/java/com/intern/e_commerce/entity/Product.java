package com.intern.e_commerce.entity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.persistence.*;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Product extends BaseEntity {
    String name;
    Long price;
    String unit;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "product")
    List<ProductImage> images = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "product", cascade = CascadeType.ALL)
    List<OrderDetail> orderDetailList = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoryId")
    Category category;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "products")
    Set<Cart> carts = new HashSet<>();
}
