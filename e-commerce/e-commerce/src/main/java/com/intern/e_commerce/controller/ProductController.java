package com.intern.e_commerce.controller;

import com.intern.e_commerce.dto.request.ProductCreateRequest;
import com.intern.e_commerce.dto.request.ProductUpdateRequest;
import com.intern.e_commerce.dto.response.ApiResponse;
import com.intern.e_commerce.dto.response.ProductResponse;
import com.intern.e_commerce.entity.Product;
import com.intern.e_commerce.service.ProductService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping(path ="/products",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<ProductResponse> createProduct(@ModelAttribute ProductCreateRequest productCreateRequest) throws IOException {
        return ApiResponse.<ProductResponse>builder()
                .result(productService.createProduct(productCreateRequest))
                .build();
    }

    @GetMapping("")
    public ApiResponse<List<ProductResponse>> getProduct() {
        return ApiResponse.<List<ProductResponse>>builder()
                .result(productService.getAllProduct())
                .build();
    }

    @GetMapping("/search")
    public ApiResponse<List<ProductResponse>> findProductByName(@Param("keyword") String keyword) {
        return ApiResponse.<List<ProductResponse>>builder()
                .result(productService.findProductByName(keyword))
                .build();
    }

    @PutMapping("/{id}")
    ApiResponse<ProductResponse> updateProduct(@PathVariable Long id,@Valid  @RequestBody ProductUpdateRequest request) throws IOException {
        return ApiResponse.<ProductResponse>builder()
                .result(productService.updateProduct(id, request))
                .build();
    }

    @DeleteMapping("/{id}")
    ApiResponse<String> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ApiResponse.<String>builder()
                .result("Product deleted.")
                .build();
    }
}
