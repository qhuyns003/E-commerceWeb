package com.intern.e_commerce.controller;

import com.intern.e_commerce.dto.request.ProductCreateRequest;
import com.intern.e_commerce.dto.response.ApiResponse;
import com.intern.e_commerce.dto.response.ProductResponse;
import com.intern.e_commerce.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
public class ProductController {

    @Autowired
    private ProductService productService;


    @PostMapping(path ="/products",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<ProductResponse> createProduct(@ModelAttribute ProductCreateRequest productCreateRequest) throws IOException {
        System.out.println("lol");
        return ApiResponse.<ProductResponse>builder()
                .result(productService.createProduct(productCreateRequest))
                .build();
    }
}
