package com.intern.e_commerce.controller;

import java.io.IOException;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.intern.e_commerce.dto.response.ApiResponse;
import com.intern.e_commerce.service.GoogleAuthService;
import com.nimbusds.jose.*;

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
