package com.intern.e_commerce.controller;

import com.intern.e_commerce.dto.request.RoleRequest;
import com.intern.e_commerce.dto.request.RoleUpdateRequest;
import com.intern.e_commerce.dto.response.ApiResponse;
import com.intern.e_commerce.dto.response.RoleResponse;
import com.intern.e_commerce.mapper.RoleMapper;
import com.intern.e_commerce.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/roles")
@RestController
public class RoleController {
    @Autowired
    private RoleService roleService;

    @Autowired
    private RoleMapper roleMapper;

    @GetMapping
    ApiResponse<List<RoleResponse>> getRoles() {
        return ApiResponse.<List<RoleResponse>>builder()
                .result(roleService.getAll())
                .build();
    }

    @DeleteMapping("/{id}")
    ApiResponse<String> deleteRole(@PathVariable String id) {
        roleService.deleteRole(id);
        return ApiResponse.<String>builder().result("succesfully deleted").build();
    }

    @PostMapping
    ApiResponse<RoleResponse> createRole(@RequestBody RoleRequest roleRequest) {
        return ApiResponse.<RoleResponse>builder()
                .result(roleService.createRole(roleRequest))
                .build();
    }

    @PutMapping("/{id}")
    ApiResponse<RoleResponse> updateRole(@PathVariable String id, @RequestBody RoleUpdateRequest roleUpdateRequest) {
        return ApiResponse.<RoleResponse>builder()
                .result(roleService.updateRole(id, roleUpdateRequest))
                .build();
    }
}
