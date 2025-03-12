package com.intern.e_commerce.service;

import com.intern.e_commerce.entity.Role;
import com.intern.e_commerce.entity.UserEntity;
import com.intern.e_commerce.enums.AuthProvider;
import com.intern.e_commerce.exception.AppException;
import com.intern.e_commerce.exception.ErrorCode;
import com.intern.e_commerce.repository.RoleRepository;
import com.intern.e_commerce.repository.UserRepositoryInterface;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    UserRepositoryInterface userRepository;
    RoleRepository roleRepository;
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        OAuth2User oauth2User = super.loadUser(userRequest);
        Map<String, Object> attributes = oauth2User.getAttributes();

        // Lấy thông tin từ Google
        String email = (String) attributes.get("email");
        String name = (String) attributes.get("name");
        if(userRepository.findByEmail(email)!=null) {
            throw  new AppException(ErrorCode.MAIL_EXISTED);
        }
        userRepository.findByEmail(email).orElseGet(() -> {
            Set<Role> roles = new HashSet<>();
            roles.add(roleRepository.findById("USER").orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND)));
            UserEntity user = new UserEntity().builder()
                    .email(email)
                    .username(name)
                    .roles(roles)
                    .authProvider(AuthProvider.GOOGLE)
                    .build();

            return userRepository.save(user);
        });

        return oauth2User;
    }
}
