package com.intern.e_commerce.controller;


import com.intern.e_commerce.dto.request.CreateURoleRequest;
import com.intern.e_commerce.dto.request.PasswordChangingRequest;
import com.intern.e_commerce.dto.request.UserCreateRequest;
import com.intern.e_commerce.dto.request.UserUpdateRequest;
import com.intern.e_commerce.dto.response.*;
import com.intern.e_commerce.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.Set;

@Slf4j
@RestController
public class User {
    @Autowired
    private UserService userService;

    @Operation(summary = "create user", description = "create user")
    @PostMapping("/users")
    ApiResponse<UserResponse> createUser(@Valid @RequestBody UserCreateRequest userCreateRequest) {
        return ApiResponse.<UserResponse>builder()
                .result(userService.createUser(userCreateRequest))
                .build();
    }

  

    @GetMapping("/myInfo")
    ApiResponse<UserResponse> getMyInfo() {
        return ApiResponse.<UserResponse>builder()
                .result(userService.getMyInfo())
                .build();
    }

    @GetMapping("/{userId}")
    ApiResponse<UserResponse> getUser(@PathVariable String userId) {
        return ApiResponse.<UserResponse>builder()
                .result(userService.getUser(userId))
                .build();
    }

    @PutMapping("/{userId}")
    ApiResponse<UserResponse> updateUser(
            @PathVariable String userId, @Valid @RequestBody UserUpdateRequest userUpdateRequest) {
        return ApiResponse.<UserResponse>builder()
                .result(userService.updateUser(userId, userUpdateRequest))
                .build();
    }

    @DeleteMapping("/{userId}")
    ApiResponse<String> deleteUser(@PathVariable String userId) {
        userService.deleteUser(userId);
        log.info("Delete users controller ...");
        return ApiResponse.<String>builder().result("User deleted").build();
    }

    @PutMapping("/passwordChanging")
    ApiResponse<UserResponse> changePassword(@RequestBody PasswordChangingRequest request){
        return ApiResponse.<UserResponse>builder()
                .result(userService.changePassword(request))
                .build();
    }
    @PostMapping("/userRole")
    ApiResponse<CreateURoleResponse> createURole (@RequestBody CreateURoleRequest request){
        return ApiResponse.<CreateURoleResponse>builder()
                .result(userService.createURole(request))
                .build();
    }

    @GetMapping("/userPermisiion/{id}")
    ApiResponse<UserPermissionRespone> getUserPermisiion(@PathVariable String id){
        return ApiResponse.<UserPermissionRespone>builder()
                .result(userService.getUserPermission(id))
                .build();
    }

}
