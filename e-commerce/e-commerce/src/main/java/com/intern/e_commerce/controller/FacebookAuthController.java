package com.intern.e_commerce.controller;

import org.springframework.web.bind.annotation.*;

import com.intern.e_commerce.dto.response.ApiResponse;
import com.intern.e_commerce.service.FacebookAuthService;

@RestController
@RequestMapping("/auth")
public class FacebookAuthController {

    private final FacebookAuthService facebookAuthService;

    public FacebookAuthController(FacebookAuthService facebookAuthService) {
        this.facebookAuthService = facebookAuthService;
    }

    @GetMapping("/facebook-login")
    public ApiResponse<String> facebookLogin(@RequestParam String code) {
        String jwt = facebookAuthService.facebookLogin(code);
        return ApiResponse.<String>builder().result(jwt).build();
    }
}
