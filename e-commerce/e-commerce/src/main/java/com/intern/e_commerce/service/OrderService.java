package com.intern.e_commerce.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.intern.e_commerce.dto.request.OrderCreateRequest;
import com.intern.e_commerce.dto.request.OrderDetailCreateRequest;
import com.intern.e_commerce.dto.response.OrderDetailResponse;
import com.intern.e_commerce.dto.response.OrderResponse;
import com.intern.e_commerce.dto.response.ProductResponse;
import com.intern.e_commerce.entity.*;
import com.intern.e_commerce.enums.OrderStatus;
import com.intern.e_commerce.exception.AppException;
import com.intern.e_commerce.exception.ErrorCode;
import com.intern.e_commerce.mapper.OrderDetailMapper;
import com.intern.e_commerce.mapper.OrderMapper;
import com.intern.e_commerce.mapper.ProductMapper;
import com.intern.e_commerce.repository.OrderRepository;
import com.intern.e_commerce.repository.ProductRepository;
import com.intern.e_commerce.repository.UserRepositoryInterface;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Transactional
public class OrderService {

    @Autowired
    private UserRepositoryInterface userRepository;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderDetailMapper orderDetailMapper;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    public List<OrderResponse> getOrderOfUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity user =
                userRepository.findByUsername(username).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        List<OrderResponse> orders = new ArrayList<>();
        for (Orders order : user.getOrderList()) {
            OrderResponse orderResponse = orderMapper.toOrderResponse(order);
            List<OrderDetailResponse> details = new ArrayList<>();
            for (OrderDetail orderDetail : order.getOrderDetailList()) {
                OrderDetailResponse orderDetailResponse = orderDetailMapper.toOrderDetailResponse(orderDetail);
                orderDetailResponse.setProduct(productMapper.toProductResponse(orderDetail.getProduct()));
                details.add(orderDetailResponse);
            }
            orderResponse.setOrderDetailResponseList(details);
            orders.add(orderResponse);
        }
        return orders;
    }

    public void createOrders(List<OrderCreateRequest> orderCreateRequests) {
        for (OrderCreateRequest orderCreateRequest : orderCreateRequests) {
            Orders orders = orderMapper.toOrders(orderCreateRequest);
            List<OrderDetail> orderDetails = new ArrayList<>();
            for (OrderDetailCreateRequest orderDetailCreateRequest : orderCreateRequest.getOrderDetails()) {

                Product product = productRepository
                        .findById(orderDetailCreateRequest.getProductId().intValue())
                        .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));
                OrderDetail orderDetail = OrderDetail.builder()
                        .order(orders)
                        .price(product.getPrice())
                        .quantity(orderDetailCreateRequest.getQuantity())
                        .product(product)
                        .build();
                orderDetails.add(orderDetail);
            }
            orders.setOrderDetailList(orderDetails);
            orders.setOrderStatus(OrderStatus.PROCESSING);
            UserEntity user = userRepository
                    .findByUsername(SecurityContextHolder.getContext()
                            .getAuthentication()
                            .getName())
                    .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
            orders.setUser(user);
            orderRepository.save(orders);
        }
    }

    public OrderResponse myOrder(Long orderId) {
        Orders order = orderRepository.findById(orderId).orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));
        OrderResponse orderResponse = orderMapper.toOrderResponse(order);
        List<OrderDetailResponse> details = new ArrayList<>();
        for (OrderDetail orderDetail : order.getOrderDetailList()) {
            OrderDetailResponse orderDetailResponse = orderDetailMapper.toOrderDetailResponse(orderDetail);
            orderDetailResponse.setProduct(productMapper.toProductResponse(orderDetail.getProduct()));
            details.add(orderDetailResponse);
        }
        orderResponse.setOrderDetailResponseList(details);
        return orderResponse;
    }

    public void succesfullyDelivering(Long orderId) {
        Orders order = orderRepository.findById(orderId).orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));
        order.setOrderStatus(OrderStatus.DELIVERED);
        orderRepository.save(order);
    }

    public OrderResponse getPaymentOrder(List<Long> ids) {
        List<OrderDetailResponse> details = new ArrayList<>();
        for (Long id : ids) {
            Product product = productRepository
                    .findById(id.intValue())
                    .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));
            ProductResponse productResponse = productMapper.toProductResponse(product);
            details.add(new OrderDetailResponse()
                    .builder()
                    .price(product.getPrice())
                    .product(productResponse)
                    .build());
        }
        return new OrderResponse().builder().orderDetailResponseList(details).build();
    }
}
