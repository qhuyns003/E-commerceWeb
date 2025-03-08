package com.intern.e_commerce.controller;

import org.springframework.web.bind.annotation.*;

import com.intern.e_commerce.dto.request.CartAddProductRequest;
import com.intern.e_commerce.dto.request.CartRemoveProductRequest;
import com.intern.e_commerce.dto.response.ApiResponse;
import com.intern.e_commerce.dto.response.CartResponse;
import com.intern.e_commerce.service.CartService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class CartController {

    CartService cartService;

    @Operation(summary = "Lấy tất cả sản phẩm có trong giỏ của user hiện tại", description = "")
    @GetMapping("/products")
    public ApiResponse<CartResponse> getAll() {
        return ApiResponse.<CartResponse>builder().result(cartService.getAll()).build();
    }

    @Operation(summary = "Thêm sản phẩm vào giỏ của user hiện tại", description = "")
    @PutMapping("/products")
    public ApiResponse<CartResponse> addProduct(@RequestBody CartAddProductRequest request) {
        // chi them vao gio hang 1 san pham 1 thoi diem thoi
        return ApiResponse.<CartResponse>builder()
                .result(cartService.addProduct(request))
                .build();
    }

    @Operation(summary = "Xóa sản phẩm được chọn khỏi giỏ của user hiện tại", description = "")
    @DeleteMapping("/products")
    public ApiResponse<CartResponse> removeProduct(@RequestBody CartRemoveProductRequest request) {
        return ApiResponse.<CartResponse>builder()
                .result(cartService.removeProduct(request))
                .build();
    }
}
