package com.intern.e_commerce.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CartAddProductRequest {
    List<Integer> productIds;
}
