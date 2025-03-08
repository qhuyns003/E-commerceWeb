package com.intern.e_commerce.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import com.intern.e_commerce.dto.request.CategoryCreateRequest;
import com.intern.e_commerce.dto.request.CategoryUpdateRequest;
import com.intern.e_commerce.dto.response.CategoryResponse;
import com.intern.e_commerce.entity.Category;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    Category toCategory(CategoryCreateRequest categoryCreateRequest);

    void updateCategory(@MappingTarget Category category, CategoryUpdateRequest categoryUpdateRequest);

    CategoryResponse toCategoryResponse(Category category);
}
