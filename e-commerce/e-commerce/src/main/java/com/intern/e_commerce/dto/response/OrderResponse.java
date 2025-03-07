package com.intern.e_commerce.dto.response;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;

import com.intern.e_commerce.enums.OrderStatus;
import com.intern.e_commerce.enums.PaymentMethod;
import com.intern.e_commerce.enums.ShippingMethod;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderResponse {
    Long id;
    OrderStatus orderStatus;
    String address;
    ShippingMethod shippingMethod;
    PaymentMethod paymentMethod;
    List<OrderDetailResponse> orderDetailResponseList = new ArrayList<>();
}
