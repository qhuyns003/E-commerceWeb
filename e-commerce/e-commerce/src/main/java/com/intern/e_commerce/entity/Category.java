package com.intern.e_commerce.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
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
    String name;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "category")
    List<Product> orderDetailList = new ArrayList<>();


}
