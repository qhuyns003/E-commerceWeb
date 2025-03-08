package com.intern.e_commerce.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.intern.e_commerce.dto.request.RoleRequest;
import com.intern.e_commerce.dto.request.RoleUpdateRequest;
import com.intern.e_commerce.dto.response.ApiResponse;
import com.intern.e_commerce.dto.response.RoleResponse;
import com.intern.e_commerce.mapper.RoleMapper;
import com.intern.e_commerce.service.RoleService;

import io.swagger.v3.oas.annotations.Operation;

@RequestMapping("/roles")
@RestController
public class RoleController {
    @Autowired
    private RoleService roleService;

    @Autowired
    private RoleMapper roleMapper;

    @Operation(summary = "Lấy toàn bộ role trên hệ thống", description = "")
    @GetMapping
    ApiResponse<List<RoleResponse>> getRoles() {
        return ApiResponse.<List<RoleResponse>>builder()
                .result(roleService.getAll())
                .build();
    }

    @Operation(summary = "Xóa 1 role", description = "không build chức năng này")
    @DeleteMapping("/{id}")
    ApiResponse<String> deleteRole(@PathVariable String id) {
        roleService.deleteRole(id);
        return ApiResponse.<String>builder().result("succesfully deleted").build();
    }

    @Operation(summary = "Tạo 1 role", description = "không build chức năng này")
    @PostMapping
    ApiResponse<RoleResponse> createRole(@RequestBody RoleRequest roleRequest) {
        return ApiResponse.<RoleResponse>builder()
                .result(roleService.createRole(roleRequest))
                .build();
    }

    @Operation(summary = "Chỉnh sửa thông tin của role", description = "chủ yếu để cấp quyền theo permission cho role")
    @PutMapping("/{id}")
    ApiResponse<RoleResponse> updateRole(@PathVariable String id, @RequestBody RoleUpdateRequest roleUpdateRequest) {
        return ApiResponse.<RoleResponse>builder()
                .result(roleService.updateRole(id, roleUpdateRequest))
                .build();
    }
}
