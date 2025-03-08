package com.intern.e_commerce.service;

import java.util.HashSet;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.intern.e_commerce.dto.request.RoleRequest;
import com.intern.e_commerce.dto.request.RoleUpdateRequest;
import com.intern.e_commerce.dto.response.RoleResponse;
import com.intern.e_commerce.entity.Role;
import com.intern.e_commerce.exception.AppException;
import com.intern.e_commerce.exception.ErrorCode;
import com.intern.e_commerce.mapper.PermissionMapper;
import com.intern.e_commerce.mapper.RoleMapper;
import com.intern.e_commerce.repository.PermissionRepository;
import com.intern.e_commerce.repository.RoleRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@Transactional
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoleService {
    final RoleRepository roleRepository;
    final RoleMapper roleMapper;
    final PermissionRepository permissionRepository;
    final PermissionMapper permissionMapper;

    public List<RoleResponse> getAll() {
        return roleRepository.findAll().stream().map(roleMapper::toResponse).toList();
    }

    public void deleteRole(String id) {
        roleRepository.deleteById(id);
    }

    public RoleResponse createRole(RoleRequest roleRequest) {
        if (roleRepository.existsById(roleRequest.getName())) {
            throw new AppException(ErrorCode.ROLE_EXISTED);
        }
        Role role = roleMapper.toEntity(roleRequest);
        role.setPermissions(new HashSet<>(permissionRepository.findAllById(roleRequest.getPermissions())));
        return roleMapper.toResponse(roleRepository.save(role));
    }

    public RoleResponse updateRole(String id, RoleUpdateRequest roleUpdateRequest) {
        Role role = roleRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));
        roleMapper.updateRole(role, roleUpdateRequest);
        role.setPermissions(new HashSet<>(permissionRepository.findAllById(roleUpdateRequest.getPermissions())));
        RoleResponse roleResponse = roleMapper.toResponse(roleRepository.save(role));
        roleResponse.setPermissions(new HashSet<>(
                role.getPermissions().stream().map(permissionMapper::toResponse).toList()));
        return roleResponse;
    }
}
