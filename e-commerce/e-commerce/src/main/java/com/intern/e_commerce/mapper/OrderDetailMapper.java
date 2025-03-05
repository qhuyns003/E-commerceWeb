package com.intern.e_commerce.mapper;


import com.intern.e_commerce.dto.request.OrderDetailCreateRequest;
import com.intern.e_commerce.dto.response.OrderDetailResponse;
import com.intern.e_commerce.dto.response.OrderResponse;
import com.intern.e_commerce.entity.OrderDetail;
import com.intern.e_commerce.entity.Orders;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderDetailMapper {

    //    @Mapping(target = "lastName",ignore = true)
//    @Mapping(target="images", ignore = true)
//    Order toProduct(ProductCreateRequest productCreateRequest);

//    @Mapping(target = "roles", ignore = true)
//    void updateUser(@MappingTarget UserEntity userEntity, UserUpdateRequest userUpdateRequest);

    //    @Mapping(source = "firstName",target = "lastName")
    @Mapping(target = "product", ignore = true)
    OrderDetailResponse toOrderDetailResponse(OrderDetail orderDetail);
}
