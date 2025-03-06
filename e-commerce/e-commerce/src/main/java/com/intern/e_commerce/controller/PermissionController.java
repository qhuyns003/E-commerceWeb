package com.intern.e_commerce.controller;


import com.intern.e_commerce.dto.request.PermissionRequest;
import com.intern.e_commerce.dto.request.PermissionUpdateRequest;
import com.intern.e_commerce.dto.response.ApiResponse;
import com.intern.e_commerce.dto.response.PermissionResponse;
import com.intern.e_commerce.mapper.PermissionMapper;
import com.intern.e_commerce.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/permissions")
@RestController
public class PermissionController {
    @Autowired
    private PermissionService permissionService;

    @Autowired
    private PermissionMapper permissionMapper;

    @GetMapping
    ApiResponse<List<PermissionResponse>> getPermissions() {
        return ApiResponse.<List<PermissionResponse>>builder()
                .result(permissionService.getAll())
                .build();
    }

    @DeleteMapping("/{id}")
    ApiResponse<String> deletePermission(@PathVariable String id) {
        permissionService.deletePermission(id);
        return ApiResponse.<String>builder().result("succesfully deleted").build();
    }

    @PostMapping
    ApiResponse<PermissionResponse> createPermission(@RequestBody PermissionRequest permissionRequest) {
        return ApiResponse.<PermissionResponse>builder()
                .result(permissionService.createPermission(permissionRequest))
                .build();
    }


    @PutMapping("/{name}")
    ApiResponse<PermissionResponse> updatePermission(@PathVariable String name, @RequestBody PermissionUpdateRequest request) {
      return ApiResponse.<PermissionResponse>builder()
              .result(permissionService.updatePermission(name,request))
              .build();
    }
}
