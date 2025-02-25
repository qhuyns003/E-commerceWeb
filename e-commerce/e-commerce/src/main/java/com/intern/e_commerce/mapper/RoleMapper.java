package com.intern.e_commerce.mapper;


import com.intern.e_commerce.dto.request.RoleRequest;
import com.intern.e_commerce.dto.request.RoleUpdateRequest;
import com.intern.e_commerce.dto.response.RoleResponse;
import com.intern.e_commerce.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    @Mapping(target = "permissions", ignore = true)
    Role toEntity(RoleRequest roleRequest);
    //    @Mapping(source = "firstName",target = "lastName")
    //    @Mapping(target = "permissions",ignore = true)
    RoleResponse toResponse(Role role);

    @Mapping(target = "permissions", ignore = true)
    void updateRole(@MappingTarget Role role, RoleUpdateRequest roleUpdateRequest);
}
