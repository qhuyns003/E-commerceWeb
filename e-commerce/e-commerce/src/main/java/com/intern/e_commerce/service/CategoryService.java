package com.intern.e_commerce.service;


import com.intern.e_commerce.dto.request.CategoryCreateRequest;
import com.intern.e_commerce.dto.request.UserCreateRequest;
import com.intern.e_commerce.dto.request.UserUpdateRequest;
import com.intern.e_commerce.dto.response.CategoryResponse;
import com.intern.e_commerce.dto.response.UserResponse;
import com.intern.e_commerce.entity.Category;
import com.intern.e_commerce.entity.Role;
import com.intern.e_commerce.entity.UserEntity;
import com.intern.e_commerce.exception.AppException;
import com.intern.e_commerce.exception.ErrorCode;
import com.intern.e_commerce.mapper.CategoryMapper;
import com.intern.e_commerce.mapper.RoleMapper;
import com.intern.e_commerce.mapper.UserMapper;
import com.intern.e_commerce.repository.CategoryRepository;
import com.intern.e_commerce.repository.RoleRepository;
import com.intern.e_commerce.repository.UserRepositoryInterface;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
@Transactional
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public CategoryService(
            CategoryMapper categoryMapper,
            CategoryRepository categoryRepository) {

        this.categoryMapper = categoryMapper;
        this.categoryRepository = categoryRepository;
    }

    public CategoryResponse createCategory(CategoryCreateRequest categoryCreateRequest) {
        Category category = categoryMapper.toCategory(categoryCreateRequest);
        category.setName(categoryCreateRequest.getName());
        try {
            category = categoryRepository.save(category);
        } catch (DataIntegrityViolationException e) {
            throw new AppException(ErrorCode.CATEGORY_EXISTED);
        }
        return categoryMapper.toCategoryResponse(category);
    }


}
