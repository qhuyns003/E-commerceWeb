package com.intern.e_commerce.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.intern.e_commerce.dto.request.ProductCreateRequest;
import com.intern.e_commerce.dto.request.ProductUpdateRequest;
import com.intern.e_commerce.dto.response.ProductResponse;
import com.intern.e_commerce.entity.Product;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    //    @Mapping(target = "lastName",ignore = true)
    @Mapping(target = "images", ignore = true)
    @Mapping(target = "category", ignore = true)
    Product toProduct(ProductCreateRequest productCreateRequest);

    //    @Mapping(target = "roles", ignore = true)
    //    void updateUser(@MappingTarget UserEntity userEntity, UserUpdateRequest userUpdateRequest);
    //    @Mapping(source = "firstName",target = "lastName")
    //    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "images", ignore = true)
    @Mapping(target = "category", ignore = true)
    ProductResponse toProductResponse(Product product);

    @Mapping(target = "images", ignore = true)
    void updateProduct(@MappingTarget Product product, ProductUpdateRequest request);
}
