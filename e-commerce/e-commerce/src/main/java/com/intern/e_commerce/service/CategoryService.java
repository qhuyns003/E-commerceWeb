package com.intern.e_commerce.service;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.intern.e_commerce.dto.request.CategoryCreateRequest;
import com.intern.e_commerce.dto.request.CategoryUpdateRequest;
import com.intern.e_commerce.dto.response.CategoryResponse;
import com.intern.e_commerce.entity.Category;
import com.intern.e_commerce.exception.AppException;
import com.intern.e_commerce.exception.ErrorCode;
import com.intern.e_commerce.mapper.CategoryMapper;
import com.intern.e_commerce.repository.CategoryRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public CategoryService(CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    public CategoryResponse createCategory(CategoryCreateRequest categoryCreateRequest) {
        Category category = categoryMapper.toCategory(categoryCreateRequest);
        try {
            category = categoryRepository.save(category);
        } catch (DataIntegrityViolationException e) {
            throw new AppException(ErrorCode.CATEGORY_EXISTED);
        }
        return categoryMapper.toCategoryResponse(category);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<CategoryResponse> getCategory() {
        return categoryRepository.findAll().stream()
                .map(categoryMapper::toCategoryResponse)
                .toList();
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public CategoryResponse updateCategory(Long id, CategoryUpdateRequest categoryUpdateRequest) {
        Category category =
                categoryRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_EXISTED));
        categoryMapper.updateCategory(category, categoryUpdateRequest);
        categoryRepository.save(category);
        return categoryMapper.toCategoryResponse(category);
    }
}
