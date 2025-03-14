package com.intern.e_commerce.controller;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.intern.e_commerce.dto.request.CreateURoleRequest;
import com.intern.e_commerce.dto.request.PasswordChangingRequest;
import com.intern.e_commerce.dto.request.UserCreateRequest;
import com.intern.e_commerce.dto.request.UserUpdateRequest;
import com.intern.e_commerce.dto.response.*;
import com.intern.e_commerce.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class User {
    @Autowired
    private UserService userService;

    @Operation(summary = "Tạo mới tài khoản", description = "hiện lên trong trang đăng kí user")
    @PostMapping("/users")
    ApiResponse<UserResponse> createUser(@Valid @RequestBody UserCreateRequest userCreateRequest) {
        return ApiResponse.<UserResponse>builder()
                .result(userService.createUser(userCreateRequest))
                .build();
    }

    @Operation(summary = "Lấy toàn bộ danh sách user trên hệ thống", description = "")
    @GetMapping("/users")
    ApiResponse<List<UserResponse>> getUser() {
        return ApiResponse.<List<UserResponse>>builder()
                .result(userService.getUser())
                .build();
    }

    @Operation(summary = "Lấy thông tin cá nhân của user đang login", description = "")
    @GetMapping("/myInfo")
    ApiResponse<UserResponse> getMyInfo() {
        return ApiResponse.<UserResponse>builder()
                .result(userService.getMyInfo())
                .build();
    }

    @Operation(summary = "Lấy thông tin của user có id trên url", description = "")
    @GetMapping("/users/{userId}")
    ApiResponse<UserResponse> getUser(@PathVariable String userId) {
        return ApiResponse.<UserResponse>builder()
                .result(userService.getUser(userId))
                .build();
    }

    @Operation(summary = "Cập nhật thông tin user hiện tại", description = "")
    @PutMapping("/{userId}")
    ApiResponse<UserResponse> updateUser(
            @PathVariable String userId, @Valid @RequestBody UserUpdateRequest userUpdateRequest) {
        return ApiResponse.<UserResponse>builder()
                .result(userService.updateUser(userId, userUpdateRequest))
                .build();
    }

    @Operation(summary = "Xóa user có id trên url", description = "")
    @DeleteMapping("/{userId}")
    ApiResponse<String> deleteUser(@PathVariable String userId) {
        userService.deleteUser(userId);
        return ApiResponse.<String>builder().result("User deleted").build();
    }

    @Operation(summary = "Thay đổi mật khẩu user hiện tại", description = "")
    @PutMapping("/passwordChanging")
    ApiResponse<UserResponse> changePassword(@RequestBody PasswordChangingRequest request) {
        return ApiResponse.<UserResponse>builder()
                .result(userService.changePassword(request))
                .build();
    }

    @Operation(summary = "tạo 1 role mới trên hệ thống", description = "")
    @PostMapping("/userRole")
    ApiResponse<CreateURoleResponse> createURole(@RequestBody CreateURoleRequest request) {
        return ApiResponse.<CreateURoleResponse>builder()
                .result(userService.createURole(request))
                .build();
    }

    @Operation(summary = "Lấy danh sách các permission của user có id trên url", description = "")
    @GetMapping("/userPermission/{id}")
    ApiResponse<UserPermissionRespone> getUserPermission(@PathVariable String id) {
        return ApiResponse.<UserPermissionRespone>builder()
                .result(userService.getUserPermission(id))
                .build();
    }
}
