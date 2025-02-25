package com.intern.e_commerce.controller;

import com.intern.e_commerce.dto.request.ProductCreateRequest;
import com.intern.e_commerce.dto.response.ApiResponse;
import com.intern.e_commerce.dto.response.ProductResponse;
import com.intern.e_commerce.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping("/products")
    ApiResponse<ProductResponse> createProduct(@RequestBody ProductCreateRequest productCreateRequest) {
        return ApiResponse.<ProductResponse>builder()
                .result(productService.createProduct(productCreateRequest))
                .build();
    }

    @GetMapping("/products/{userName}")
    ApiResponse<List<ProductResponse>> getAllProductsOfUser(@PathVariable String userName) {
        return ApiResponse.<List<ProductResponse>>builder()
                .result(productService.getAllProductsOfUser(userName))
                .build();
    }


}
