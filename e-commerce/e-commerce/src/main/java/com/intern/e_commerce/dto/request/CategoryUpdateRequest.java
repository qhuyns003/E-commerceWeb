package com.intern.e_commerce.dto.request;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
public class CategoryUpdateRequest {
    String name;
}
