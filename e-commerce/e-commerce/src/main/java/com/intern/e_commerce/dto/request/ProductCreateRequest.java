package com.intern.e_commerce.dto.request;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductCreateRequest {
    String name;
    Long price;
    String unit;
    List<MultipartFile> images;
    Long categoryId;
}
