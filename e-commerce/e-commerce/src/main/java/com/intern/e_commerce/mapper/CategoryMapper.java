package com.intern.e_commerce.mapper;


import com.intern.e_commerce.dto.request.CategoryCreateRequest;
import com.intern.e_commerce.dto.request.CategoryUpdateRequest;
import com.intern.e_commerce.dto.request.UserCreateRequest;
import com.intern.e_commerce.dto.request.UserUpdateRequest;
import com.intern.e_commerce.dto.response.CategoryResponse;
import com.intern.e_commerce.dto.response.UserResponse;
import com.intern.e_commerce.entity.Category;
import com.intern.e_commerce.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    Category toCategory(CategoryCreateRequest categoryCreateRequest);

    void updateCategory(@MappingTarget Category category, CategoryUpdateRequest categoryUpdateRequest);

    CategoryResponse toCategoryResponse(Category category);
}
