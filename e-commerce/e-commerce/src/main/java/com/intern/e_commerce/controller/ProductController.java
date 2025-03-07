package com.intern.e_commerce.controller;

import java.io.IOException;
import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import com.intern.e_commerce.dto.request.ProductCreateRequest;
import com.intern.e_commerce.dto.request.ProductUpdateRequest;
import com.intern.e_commerce.dto.response.ApiResponse;
import com.intern.e_commerce.dto.response.ProductResponse;
import com.intern.e_commerce.service.ProductService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Operation(summary = "tạo mới 1 sản phẩm", description = "gửi dưới dạng  form-data, có upload file ảnh")
    @PostMapping(path = "/products", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<ProductResponse> createProduct(@ModelAttribute ProductCreateRequest productCreateRequest)
            throws IOException {
        return ApiResponse.<ProductResponse>builder()
                .result(productService.createProduct(productCreateRequest))
                .build();
    }

    @Operation(summary = "Lấy toàn bộ danh sách sản phẩm trên hệ thống", description = "")
    @GetMapping("")
    public ApiResponse<List<ProductResponse>> getProduct() {
        return ApiResponse.<List<ProductResponse>>builder()
                .result(productService.getAllProduct())
                .build();
    }

    @Operation(summary = "Tìm kiếm theo tên sản phẩm", description = "")
    @GetMapping("/search")
    public ApiResponse<List<ProductResponse>> findProductByName(@Param("keyword") String keyword) {
        return ApiResponse.<List<ProductResponse>>builder()
                .result(productService.findProductByName(keyword))
                .build();
    }

    @Operation(summary = "Cập nhật thông tin sản phẩm", description = "")
    @PutMapping("/{id}")
    ApiResponse<ProductResponse> updateProduct(@PathVariable Long id, @Valid @RequestBody ProductUpdateRequest request)
            throws IOException {
        return ApiResponse.<ProductResponse>builder()
                .result(productService.updateProduct(id, request))
                .build();
    }

    @Operation(summary = "Xóa sản phẩm có id trên url", description = "")
    @DeleteMapping("/{id}")
    ApiResponse<String> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ApiResponse.<String>builder().result("Product deleted.").build();
    }
}
