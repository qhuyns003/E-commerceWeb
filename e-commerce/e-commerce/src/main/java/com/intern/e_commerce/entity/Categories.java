package com.intern.e_commerce.entity;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
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
public class Categories extends BaseEntity {
    String name;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "categories",cascade = CascadeType.ALL)
    List<Product> orderDetailList = new ArrayList<>();


}
