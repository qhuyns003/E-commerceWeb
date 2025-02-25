package com.intern.e_commerce.entity;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Product extends BaseEntity {
    String name;
    Long price;
    String unit;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name= "userId")
    UserEntity user;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "product")
    List<OrderDetail> orderDetailList = new ArrayList<>();


}
