package com.intern.e_commerce.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.intern.e_commerce.entity.Role;
import com.intern.e_commerce.entity.UserEntity;
import com.intern.e_commerce.exception.AppException;
import com.intern.e_commerce.exception.ErrorCode;
import com.intern.e_commerce.repository.RoleRepository;
import com.intern.e_commerce.repository.UserRepositoryInterface;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class FacebookAuthService {
    private final UserRepositoryInterface userRepository;
    private final RoleRepository roleRepository;
    private final AuthenticationService authenticationService;

    @Value("${facebook.client.id}")
    private String facebookClientId;

    @Value("${facebook.client.secret}")
    private String facebookClientSecret;

    @Value("${facebook.redirect.uri}")
    private String redirectUri;

    public FacebookAuthService(UserRepositoryInterface userRepository, RoleRepository roleRepository, AuthenticationService authenticationService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.authenticationService = authenticationService;
    }

    public String facebookLogin(String code) {
        // 1. Lấy access token từ Facebook
        String accessToken = getFacebookAccessToken(code);

        // 2. Lấy thông tin user từ Facebook
        Map<String, String> userInfo = getFacebookUserInfo(accessToken);

        String email = userInfo.get("id");
        String name = userInfo.get("name");

        if (email == null) {
            throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }

        // 3. Kiểm tra user trong database
        Optional<UserEntity> userOptional = userRepository.findByUsername(email);
        UserEntity user;
        if (userOptional.isPresent()) {
            user = userOptional.get();
        } else {
            user = new UserEntity();
            user.setUsername(email);
            user.setFirstName(name);
            Set<Role> roleSet = new HashSet<>();
            roleSet.add(roleRepository.findById(com.intern.e_commerce.enums.Role.USER.name()).orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND)));
            user.setRoles(roleSet);
            userRepository.save(user);
        }

        // 4. Tạo JWT token
        return authenticationService.generateToken(user);
    }

    private String getFacebookAccessToken(String code) {
        String url = "https://graph.facebook.com/v18.0/oauth/access_token?"
                + "client_id=" + facebookClientId
                + "&client_secret=" + facebookClientSecret
                + "&redirect_uri=" + redirectUri
                + "&code=" + code;

        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject(url, String.class);

        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(response);
            return rootNode.get("access_token").asText();
        } catch (Exception e) {
            throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }
    }

    private Map<String, String> getFacebookUserInfo(String accessToken) {
        String url = "https://graph.facebook.com/me?fields=id,name,email&access_token=" + accessToken;

        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject(url, String.class);

        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(response);
            Map<String, String> userInfo = new HashMap<>();
            userInfo.put("id", rootNode.get("id").asText());
            userInfo.put("name", rootNode.get("name").asText());
            if (rootNode.has("email")) {
                userInfo.put("email", rootNode.get("email").asText());
            }
            return userInfo;
        } catch (Exception e) {
            throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }
    }
}
