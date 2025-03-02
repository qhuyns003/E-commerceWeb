package com.intern.e_commerce.dto.response;

import com.intern.e_commerce.entity.BaseEntity;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoryResponse extends BaseEntity {
    String name;
}
