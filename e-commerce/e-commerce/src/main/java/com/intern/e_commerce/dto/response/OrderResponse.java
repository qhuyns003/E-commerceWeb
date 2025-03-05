package com.intern.e_commerce.dto.response;

import com.intern.e_commerce.entity.OrderDetail;
import com.intern.e_commerce.entity.UserEntity;
import com.intern.e_commerce.enums.OrderStatus;
import com.intern.e_commerce.enums.PaymentMethod;
import com.intern.e_commerce.enums.ShippingMethod;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

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
