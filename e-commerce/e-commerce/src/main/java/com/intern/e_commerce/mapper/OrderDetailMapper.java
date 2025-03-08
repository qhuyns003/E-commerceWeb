package com.intern.e_commerce.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.intern.e_commerce.dto.response.OrderDetailResponse;
import com.intern.e_commerce.entity.OrderDetail;

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
