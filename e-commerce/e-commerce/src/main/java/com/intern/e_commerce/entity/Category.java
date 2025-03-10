package com.intern.e_commerce.entity;


import jakarta.persistence.*;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Category extends BaseEntity {

    @Column(unique = true)
    String name;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "category",cascade = CascadeType.ALL)
    List<Product> orderDetailList = new ArrayList<>();


}
