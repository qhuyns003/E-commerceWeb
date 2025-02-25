package com.intern.e_commerce.mapper;


import com.intern.e_commerce.dto.request.UserCreateRequest;
import com.intern.e_commerce.dto.request.UserUpdateRequest;
import com.intern.e_commerce.dto.response.UserResponse;
import com.intern.e_commerce.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {

    //    @Mapping(target = "lastName",ignore = true)
    UserEntity toUser(UserCreateRequest userCreateRequest);

    @Mapping(target = "roles", ignore = true)
    void updateUser(@MappingTarget UserEntity userEntity, UserUpdateRequest userUpdateRequest);

    //    @Mapping(source = "firstName",target = "lastName")
    @Mapping(target = "roles", ignore = true)
    UserResponse toUserResponse(UserEntity userEntity);
}
