package com.intern.e_commerce.configuration;


import com.intern.e_commerce.controller.User;
import com.intern.e_commerce.entity.Permission;
import com.intern.e_commerce.entity.UserEntity;
import com.intern.e_commerce.enums.Role;
import com.intern.e_commerce.exception.AppException;
import com.intern.e_commerce.exception.ErrorCode;
import com.intern.e_commerce.repository.PermissionRepository;
import com.intern.e_commerce.repository.RoleRepository;
import com.intern.e_commerce.repository.UserRepositoryInterface;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Slf4j
@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Transactional
public class ApplicationConfig {
    static String userName = "admin";
    final RoleRepository roleRepository;
    final PasswordEncoder passwordEncoder;
    final PermissionRepository permissionRepository;
    @Bean
    @ConditionalOnProperty(
            prefix = "spring",
            value = "datasource.driverClassName",
            havingValue = "com.mysql.cj.jdbc.Driver")
    ApplicationRunner applicationRunner(UserRepositoryInterface userRepository, User user) {
        log.info("Application started...");
        return args -> {
            if (!roleRepository.existsById(Role.ADMIN.name())) {
                Permission permission = Permission.builder()
                        .name("CREATE_POST")
                        .description("create post")
                        .build();
                permissionRepository.save(permission);
                Set<Permission> permissions = new HashSet<>();
                permissions.add(permission);
                com.intern.e_commerce.entity.Role role = com.intern.e_commerce.entity.Role.builder()
                        .name(Role.ADMIN.name())
                        .description("Administrator")
                        .permissions(permissions)
                        .build();
                roleRepository.save(role);
            }

            if (!roleRepository.existsById(Role.USER.name())) {
                Permission permission = Permission.builder()
                        .name("CREATE_POST")
                        .description("create post")
                        .build();
                permissionRepository.save(permission);
                Set<Permission> permissions = new HashSet<>();
                permissions.add(permission);
                com.intern.e_commerce.entity.Role role = com.intern.e_commerce.entity.Role.builder()
                        .name(Role.USER.name())
                        .description("User")
                        .permissions(permissions)
                        .build();
                roleRepository.save(role);
            }

            if (userRepository.findByUsername(userName).isEmpty()) {
                Set<com.intern.e_commerce.entity.Role> roles = new HashSet<>();
                com.intern.e_commerce.entity.Role role = roleRepository
                        .findById(com. intern. e_commerce. enums. Role.ADMIN.name())
                        .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));
                roles.add(role);
                UserEntity userEntity = UserEntity.builder()
                        .username(userName)
                        .password(passwordEncoder.encode("admin"))
                        .roles(roles)
                        .build();
                userRepository.save(userEntity);
            }
        };
    }
}
