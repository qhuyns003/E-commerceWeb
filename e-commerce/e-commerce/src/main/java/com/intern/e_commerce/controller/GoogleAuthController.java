 package com.intern.e_commerce.controller;

 import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
 import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
 import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
 import com.google.api.client.http.javanet.NetHttpTransport;
 import com.google.api.client.json.jackson2.JacksonFactory;
 import com.intern.e_commerce.dto.response.ApiResponse;
 import com.intern.e_commerce.entity.Role;
 import com.intern.e_commerce.entity.UserEntity;
 import com.intern.e_commerce.exception.AppException;
 import com.intern.e_commerce.exception.ErrorCode;
 import com.intern.e_commerce.repository.RoleRepository;
 import com.intern.e_commerce.repository.UserRepositoryInterface;
 import com.intern.e_commerce.service.AuthenticationService;
 import com.intern.e_commerce.service.GoogleAuthService;
 import com.nimbusds.jose.*;
 import com.nimbusds.jose.crypto.MACSigner;
 import com.nimbusds.jwt.JWTClaimsSet;
 import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.beans.factory.annotation.Value;
 import org.springframework.http.ResponseEntity;
 import org.springframework.util.CollectionUtils;
 import org.springframework.web.bind.annotation.*;

 import java.io.IOException;
 import java.time.Instant;
 import java.time.temporal.ChronoUnit;
 import java.util.*;

 @RestController
 @RequestMapping("/auth")
 public class GoogleAuthController {
     @Autowired
     private GoogleAuthService googleAuthService;
     @GetMapping("/google")
     public ApiResponse<String> googleLogin(@RequestParam("code") String code) throws IOException {
         return ApiResponse.<String>builder()
                 .result(googleAuthService.googleLogin(code))
                 .build();
     }
 }
