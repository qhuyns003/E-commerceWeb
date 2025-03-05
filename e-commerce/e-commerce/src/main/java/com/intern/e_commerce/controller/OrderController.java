package com.intern.e_commerce.controller;

import com.intern.e_commerce.dto.request.OrderCreateRequest;
import com.intern.e_commerce.dto.request.ProductCreateRequest;
import com.intern.e_commerce.dto.response.ApiResponse;
import com.intern.e_commerce.dto.response.OrderResponse;
import com.intern.e_commerce.dto.response.ProductResponse;
import com.intern.e_commerce.service.OrderService;
import com.intern.e_commerce.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping( "/order")
public class OrderController {

    @Autowired
    private OrderService orderService;



    @GetMapping()
    public ApiResponse<List<OrderResponse>> getOrderOfUser(){
        return ApiResponse.<List<OrderResponse>>builder()
                .result(orderService.getOrderOfUser())
                .build();
    }
    @PostMapping()
    public ApiResponse<?> createOrderByProductIds(@RequestBody List<OrderCreateRequest> orderCreateRequests){
        orderService.createOrders(orderCreateRequests);
        return ApiResponse.builder()
                .result("success")
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<?> myOrder(@PathVariable Long id){
        return ApiResponse.builder()
                .result(orderService.myOrder(id))
                .build();
    }

    @PutMapping("successfulDelivering/{id}")
    public ApiResponse<?> successfulDelivering(@PathVariable Long id){
        orderService.succesfullyDelivering(id);
        return ApiResponse.builder()
                .result("success")
                .build();
    }

    @GetMapping("/payment")
    public ApiResponse<?> getPaymentOrder(@RequestParam List<Long> ids){
        return ApiResponse.builder()
                .result(orderService.getPaymentOrder(ids))
                .build();
    }





}
