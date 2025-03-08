package com.intern.e_commerce.dto.request;

import java.util.List;

import com.intern.e_commerce.enums.PaymentMethod;
import com.intern.e_commerce.enums.ShippingMethod;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderCreateRequest {
    List<OrderDetailCreateRequest> orderDetails;
    String address;
    ShippingMethod shippingMethod;
    PaymentMethod paymentMethod;
}
