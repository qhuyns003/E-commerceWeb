package com.intern.e_commerce.mapper;


import com.intern.e_commerce.dto.request.PermissionRequest;
import com.intern.e_commerce.dto.response.PermissionResponse;
import com.intern.e_commerce.entity.Permission;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PermissionMapper {

    //    @Mapping(target = "permissions",ignore = true)
    Permission toEntity(PermissionRequest permissionRequest);

    void updatePermission(@MappingTarget Permission permission, PermissionRequest permissionRequest);

    //    @Mapping(source = "firstName",target = "lastName")

    PermissionResponse toResponse(Permission permission);
}
