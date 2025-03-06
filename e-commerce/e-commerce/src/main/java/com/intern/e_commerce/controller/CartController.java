package com.intern.e_commerce.controller;

import com.intern.e_commerce.dto.request.CartAddProductRequest;
import com.intern.e_commerce.dto.request.CartRemoveProductRequest;
import com.intern.e_commerce.dto.response.ApiResponse;
import com.intern.e_commerce.dto.response.CartResponse;
import com.intern.e_commerce.service.CartService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class CartController {

    CartService cartService;

    @GetMapping("/products")
    public ApiResponse<CartResponse> getAll(){
        return ApiResponse.<CartResponse>builder()
                .result(cartService.getAll())
                .build();
    }

    @PutMapping("/products")
    public ApiResponse<CartResponse> addProduct(@RequestBody CartAddProductRequest request){
        return ApiResponse.<CartResponse>builder()
                .result(cartService.addProduct(request))
                .build();

    }
    @DeleteMapping("/products")
    public ApiResponse<CartResponse> removeProduct(@RequestBody CartRemoveProductRequest request){
        return ApiResponse.<CartResponse>builder()
                .result(cartService.removeProduct(request))
                .build();
    }


}
