package com.intern.e_commerce.controller;


import com.intern.e_commerce.dto.request.AuthenticationRequest;
import com.intern.e_commerce.dto.request.IntrospectRequest;
import com.intern.e_commerce.dto.request.LogoutRequest;
import com.intern.e_commerce.dto.request.RefreshTokenRequest;
import com.intern.e_commerce.dto.response.ApiResponse;
import com.intern.e_commerce.dto.response.AuthenticationResponse;
import com.intern.e_commerce.dto.response.IntrospectResponse;
import com.intern.e_commerce.dto.response.RefreshTokenResponse;
import com.intern.e_commerce.service.AuthenticationService;
import com.nimbusds.jose.JOSEException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AuthenticationController {
    AuthenticationService authentication;

    @PostMapping("/log-in")
    ApiResponse<AuthenticationResponse> authenticationUser(@RequestBody AuthenticationRequest authenticationRequest) {
        return ApiResponse.<AuthenticationResponse>builder()
                .result(AuthenticationResponse.builder()
                        .token(authentication.authenticateUser(authenticationRequest))
                        .build())
                .build();
    }

    @PostMapping("/logout")
    ApiResponse<String> logout(@RequestBody LogoutRequest logoutRequest) throws ParseException, JOSEException {
        authentication.logout(logoutRequest.getToken());
        return ApiResponse.<String>builder().result("successful").build();
    }

    @PostMapping("/introspect")
    ApiResponse<IntrospectResponse> introspectUser(@RequestBody IntrospectRequest introspectRequest)
            throws ParseException, JOSEException {
        return ApiResponse.<IntrospectResponse>builder()
                .result(authentication.introspectUser(introspectRequest))
                .build();
    }

    @PostMapping("/refresh")
    ApiResponse<RefreshTokenResponse> refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest)
            throws ParseException, JOSEException {
        return ApiResponse.<RefreshTokenResponse>builder()
                .result(authentication.refreshToken(refreshTokenRequest))
                .build();
    }
}
