package com.intern.e_commerce.mapper;

import org.mapstruct.Mapper;

import com.intern.e_commerce.dto.request.OrderCreateRequest;
import com.intern.e_commerce.dto.response.OrderResponse;
import com.intern.e_commerce.entity.Orders;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    //    @Mapping(target = "lastName",ignore = true)
    //    @Mapping(target="images", ignore = true)
    //    Order toProduct(ProductCreateRequest productCreateRequest);
    Orders toOrders(OrderCreateRequest orderCreateRequest);

    //    @Mapping(target = "roles", ignore = true)
    //    void updateUser(@MappingTarget UserEntity userEntity, UserUpdateRequest userUpdateRequest);

    //    @Mapping(source = "firstName",target = "lastName")
    //    @Mapping(target = "roles", ignore = true)
    OrderResponse toOrderResponse(Orders order);
}
