package com.intern.e_commerce.controller;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.intern.e_commerce.dto.request.CategoryCreateRequest;
import com.intern.e_commerce.dto.request.CategoryUpdateRequest;
import com.intern.e_commerce.dto.response.ApiResponse;
import com.intern.e_commerce.dto.response.CategoryResponse;
import com.intern.e_commerce.service.CategoryService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/categories")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @Operation(summary = "tạo mới 1 danh mục", description = "không build fe")
    @PostMapping
    ApiResponse<CategoryResponse> createCategory(@Valid @RequestBody CategoryCreateRequest categoryCreateRequest) {
        return ApiResponse.<CategoryResponse>builder()
                .result(categoryService.createCategory(categoryCreateRequest))
                .build();
    }

    @Operation(summary = "Lấy thông tin của tất cả danh mục có trên hệ thống", description = "")
    @GetMapping
    ApiResponse<List<CategoryResponse>> getCategory() {
        return ApiResponse.<List<CategoryResponse>>builder()
                .result(categoryService.getCategory())
                .build();
    }

    @Operation(
            summary = "Xóa danh mục theo id trên url",
            description = "Lưu ý khi xóa danh mục là tất cả sản phẩm liên quan cũng bị xóa theo")
    @DeleteMapping("/{categoryId}")
    ApiResponse<String> deleteCategory(@PathVariable long categoryId) {
        categoryService.deleteCategory(categoryId);
        log.info("Delete category controller ...");
        return ApiResponse.<String>builder().result("category deleted").build();
    }

    @Operation(summary = "Chỉnh sửa thông tin danh mục có id trên url", description = "")
    @PutMapping("/{categoryId}")
    ApiResponse<CategoryResponse> updateCategory(
            @PathVariable long categoryId, @Valid @RequestBody CategoryUpdateRequest categoryUpdateRequest) {
        return ApiResponse.<CategoryResponse>builder()
                .result(categoryService.updateCategory(categoryId, categoryUpdateRequest))
                .build();
    }
}
