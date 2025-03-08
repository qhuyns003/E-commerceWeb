package com.intern.e_commerce.entity;

import jakarta.persistence.*;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductImage extends BaseEntity {

    String url;
    Long possition;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
}
