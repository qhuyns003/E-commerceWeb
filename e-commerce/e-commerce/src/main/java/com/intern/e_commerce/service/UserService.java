package com.intern.e_commerce.service;


import com.intern.e_commerce.dto.request.UserCreateRequest;
import com.intern.e_commerce.dto.request.UserUpdateRequest;
import com.intern.e_commerce.dto.response.UserResponse;
import com.intern.e_commerce.entity.Role;
import com.intern.e_commerce.entity.UserEntity;
import com.intern.e_commerce.exception.AppException;
import com.intern.e_commerce.exception.ErrorCode;
import com.intern.e_commerce.mapper.RoleMapper;
import com.intern.e_commerce.mapper.UserMapper;
import com.intern.e_commerce.repository.RoleRepository;
import com.intern.e_commerce.repository.UserRepositoryInterface;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
@Transactional
public class UserService {
    private final UserRepositoryInterface userRepository;

    private final UserMapper userMapper;

    private final RoleRepository roleRepository;

    private final RoleMapper roleMapper;

    public UserService(
            UserRepositoryInterface userRepository,
            UserMapper userMapper,
            RoleRepository roleRepository,
            RoleMapper roleMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.roleRepository = roleRepository;
        this.roleMapper = roleMapper;
    }

    public UserResponse createUser(UserCreateRequest userCreateRequest) {
        UserEntity userEntity = userMapper.toUser(userCreateRequest);
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        userEntity.setPassword(passwordEncoder.encode(userCreateRequest.getPassword()));
        Set<Role> roles = new HashSet<>();
        roles.add(roleRepository.findById("USER").orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND)));
        userEntity.setRoles(roles);
        try {
            userEntity = userRepository.save(userEntity);
        } catch (DataIntegrityViolationException e) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }
        return userMapper.toUserResponse(userEntity);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponse> getUser() {
        log.info("pass");
        List<UserEntity> userEntity = userRepository.findAll();
        List<UserResponse> userResponses = new ArrayList<>();
        for (UserEntity userEntity1 : userEntity) {
            userResponses.add(userMapper.toUserResponse(userEntity1));
        }
        return userResponses;
    }

    @PostAuthorize("returnObject.username = authentication.name")
    public UserResponse getUser(String id) {
        UserEntity userEntity = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        return userMapper.toUserResponse(userEntity);
    }

    public UserResponse updateUser(String id, UserUpdateRequest userUpdateRequest) {
        UserEntity userEntity =
                userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        userMapper.updateUser(userEntity, userUpdateRequest);
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        userEntity.setPassword(passwordEncoder.encode(userUpdateRequest.getPassword()));
        List<Role> l = roleRepository.findAllById(userUpdateRequest.getRoles());
        HashSet<Role> rr = new HashSet<>(l);
        userEntity.setRoles(rr);
        UserResponse userResponse = userMapper.toUserResponse(userRepository.save(userEntity));
        userResponse.setRoles(new HashSet<>(
                userEntity.getRoles().stream().map(roleMapper::toResponse).toList()));

        return userResponse;
    }

    public void deleteUser(String id) {
        userRepository.deleteById(id);
    }

    public UserResponse getMyInfo() {
        String id = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity userEntity =
                userRepository.findByUsername(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        return userMapper.toUserResponse(userEntity);
    }
}
