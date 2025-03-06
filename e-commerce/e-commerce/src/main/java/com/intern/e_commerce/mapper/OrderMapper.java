package com.intern.e_commerce.mapper;


import com.intern.e_commerce.dto.request.OrderCreateRequest;
import com.intern.e_commerce.dto.request.ProductCreateRequest;
import com.intern.e_commerce.dto.response.OrderResponse;
import com.intern.e_commerce.dto.response.ProductResponse;
import com.intern.e_commerce.entity.Orders;
import com.intern.e_commerce.entity.Product;
import jakarta.persistence.criteria.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

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
