package com.intern.e_commerce.controller;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.intern.e_commerce.dto.request.OrderCreateRequest;
import com.intern.e_commerce.dto.response.ApiResponse;
import com.intern.e_commerce.dto.response.OrderResponse;
import com.intern.e_commerce.service.OrderService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Operation(summary = "Lấy tất cả các đơn hàng có trên hệ thống", description = "")
    @GetMapping()
    public ApiResponse<List<OrderResponse>> getOrderOfUser() {
        return ApiResponse.<List<OrderResponse>>builder()
                .result(orderService.getOrderOfUser())
                .build();
    }

    @Operation(summary = "Tạo đơn hàng", description = "được gọi khi bấm nút đặt hàng trên view order")
    @PostMapping()
    public ApiResponse<?> createOrderByProductIds(@RequestBody List<OrderCreateRequest> orderCreateRequests) {
        orderService.createOrders(orderCreateRequests);
        return ApiResponse.builder().result("success").build();
    }

    @Operation(summary = "Xem thông tin đơn hàng có id trên url", description = "")
    @GetMapping("/{id}")
    public ApiResponse<?> myOrder(@PathVariable Long id) {
        return ApiResponse.builder().result(orderService.myOrder(id)).build();
    }

    @Operation(summary = "Xác nhận đã giao hàng của đơn hàng có id trên url", description = "admin role")
    @PutMapping("successfulDelivering/{id}")
    public ApiResponse<?> successfulDelivering(@PathVariable Long id) {
        orderService.succesfullyDelivering(id);
        return ApiResponse.builder().result("success").build();
    }

    @Operation(summary = "View order sau khi bấm thanh toán", description = "Giao diện hiện lên gồm các sản phẩm -> chọn số lượng sản phẩm cũng như phương thức thanh toán,...")
    @GetMapping("/payment")
    public ApiResponse<?> getPaymentOrder(@RequestParam List<Long> ids) {
        return ApiResponse.builder().result(orderService.getPaymentOrder(ids)).build();
    }
}
