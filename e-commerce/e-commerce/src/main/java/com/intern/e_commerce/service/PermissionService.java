package com.intern.e_commerce.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.intern.e_commerce.dto.request.PermissionRequest;
import com.intern.e_commerce.dto.request.PermissionUpdateRequest;
import com.intern.e_commerce.dto.response.PermissionResponse;
import com.intern.e_commerce.entity.Permission;
import com.intern.e_commerce.mapper.PermissionMapper;
import com.intern.e_commerce.repository.PermissionRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PermissionService {
    final PermissionRepository permissionRepository;
    final PermissionMapper permissionMapper;

    public List<PermissionResponse> getAll() {
        return permissionRepository.findAll().stream()
                .map(permissionMapper::toResponse)
                .toList();
    }

    public void deletePermission(String id) {
        permissionRepository.deleteById(id);
    }

    public PermissionResponse createPermission(PermissionRequest permissionRequest) {
        return permissionMapper.toResponse(permissionRepository.save(permissionMapper.toEntity(permissionRequest)));
    }

    public PermissionResponse updatePermission(String id, PermissionUpdateRequest permissionRequest) {
        Permission permission = permissionRepository.findByName(id);
        permission.setDescription(permissionRequest.getDescription());
        return permissionMapper.toResponse(permissionRepository.save(permission));
    }
}
