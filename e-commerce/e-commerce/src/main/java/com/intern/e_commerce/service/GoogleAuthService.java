package com.intern.e_commerce.service;

import java.io.IOException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.intern.e_commerce.entity.Role;
import com.intern.e_commerce.entity.UserEntity;
import com.intern.e_commerce.exception.AppException;
import com.intern.e_commerce.exception.ErrorCode;
import com.intern.e_commerce.repository.RoleRepository;
import com.intern.e_commerce.repository.UserRepositoryInterface;

@Service
public class GoogleAuthService {
    private final UserRepositoryInterface userRepository;
    private final RoleRepository roleRepository;
    private final AuthenticationService authenticationService;

    @Value("${google.client.id}")
    private String googleClientId;

    @Value("${google.client.secret}")
    private String googleClientSecret;

    @Value("${google.redirect.uri}")
    private String redirectUri;

    public GoogleAuthService(
            UserRepositoryInterface userRepository,
            RoleRepository roleRepository,
            AuthenticationService authenticationService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.authenticationService = authenticationService;
    }

    public String googleLogin(String code) throws IOException {
        GoogleTokenResponse tokenResponse = null; // Khai báo trước để tránh lỗi
        try {
            tokenResponse = new GoogleAuthorizationCodeTokenRequest(
                            new NetHttpTransport(),
                            JacksonFactory.getDefaultInstance(),
                            "https://oauth2.googleapis.com/token",
                            googleClientId,
                            googleClientSecret,
                            code,
                            redirectUri)
                    .execute();
        } catch (Exception e) {
            //             e.printStackTrace();
            //             return ResponseEntity.badRequest().body("Lỗi xác thực với Google!");
        }
        if (tokenResponse == null) {
            //             return ResponseEntity.badRequest().body("Không nhận được phản
        }
        String idTokenString = tokenResponse.getIdToken();
        GoogleIdToken.Payload payload = verifyGoogleToken(idTokenString);
        if (payload == null) {
            //             return ResponseEntity.badRequest().body("Token không hợp lệ!");
        }
        String id = payload.getSubject();
        Optional<UserEntity> userOptional = userRepository.findByUsername(id);

        UserEntity user;
        if (userOptional.isPresent()) {
            user = userOptional.get();
        } else {
            user = new UserEntity();
            user.setUsername(id);
            user.setFirstName((String) payload.get("name"));
            Set<Role> roleSet = new HashSet<>();
            roleSet.add(roleRepository
                    .findById(com.intern.e_commerce.enums.Role.USER.name())
                    .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND)));
            user.setRoles(roleSet);
            userRepository.save(user);
        }

        String jwt = authenticationService.generateToken(user);
        return jwt;
    }
    ;

    private GoogleIdToken.Payload verifyGoogleToken(String idTokenString) throws IOException {
        GoogleIdToken idToken = GoogleIdToken.parse(JacksonFactory.getDefaultInstance(), idTokenString);
        return (idToken != null) ? idToken.getPayload() : null;
    }
}
