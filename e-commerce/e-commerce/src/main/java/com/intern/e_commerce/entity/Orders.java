package com.intern.e_commerce.entity;


import com.intern.e_commerce.enums.OrderStatus;
import com.intern.e_commerce.enums.PaymentMethod;
import com.intern.e_commerce.enums.ShippingMethod;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Orders extends BaseEntity {

    @Enumerated(EnumType.STRING)
    OrderStatus orderStatus;
    String address;
    @Enumerated(EnumType.STRING)
    ShippingMethod shippingMethod;
    @Enumerated(EnumType.STRING)
    PaymentMethod paymentMethod;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    UserEntity user;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "order", cascade = CascadeType.ALL)
    List<OrderDetail> orderDetailList = new ArrayList<>();




}
