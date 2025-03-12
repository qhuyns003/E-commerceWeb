package com.intern.e_commerce.controller;

import com.intern.e_commerce.service.FacebookAuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class FacebookAuthController {

    private final FacebookAuthService facebookAuthService;

    public FacebookAuthController(FacebookAuthService facebookAuthService) {
        this.facebookAuthService = facebookAuthService;
    }

    @GetMapping("/facebook-login")
    public ResponseEntity<?> facebookLogin(@RequestParam String code) {
        String jwt = facebookAuthService.facebookLogin(code);
        return ResponseEntity.ok().body(jwt);
    }
}

