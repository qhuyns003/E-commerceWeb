package com.intern.e_commerce.controller;


import com.intern.e_commerce.dto.request.CategoryCreateRequest;
import com.intern.e_commerce.dto.request.UserCreateRequest;
import com.intern.e_commerce.dto.request.UserUpdateRequest;
import com.intern.e_commerce.dto.response.ApiResponse;
import com.intern.e_commerce.dto.response.CategoryResponse;
import com.intern.e_commerce.dto.response.UserResponse;
import com.intern.e_commerce.service.CategoryService;
import com.intern.e_commerce.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/categories")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;
    //thu
    @PostMapping
    ApiResponse<CategoryResponse> createCategory(@Valid @RequestBody CategoryCreateRequest categoryCreateRequest) {
        return ApiResponse.<CategoryResponse>builder()
                .result(categoryService.createCategory(categoryCreateRequest))
                .build();
    }

    @GetMapping
    ApiResponse<List<CategoryResponse>> getCategory() {
        return ApiResponse.<List<CategoryResponse>>builder()
                .result(categoryService.getCategory())
                .build();
    }


    @DeleteMapping("/{categoryId}")
    ApiResponse<String> deleteCategory(@PathVariable long categoryId) {
        categoryService.deleteCategory(categoryId);
        log.info("Delete category controller ...");
        return ApiResponse.<String>builder().result("category deleted").build();
    }


}
