package com.intern.e_commerce.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import com.intern.e_commerce.dto.request.PermissionRequest;
import com.intern.e_commerce.dto.response.PermissionResponse;
import com.intern.e_commerce.entity.Permission;

@Mapper(componentModel = "spring")
public interface PermissionMapper {

    //    @Mapping(target = "permissions",ignore = true)
    Permission toEntity(PermissionRequest permissionRequest);

    void updatePermission(@MappingTarget Permission permission, PermissionRequest permissionRequest);

    //    @Mapping(source = "firstName",target = "lastName")

    PermissionResponse toResponse(Permission permission);
}
