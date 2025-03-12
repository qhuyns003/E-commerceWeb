package com.intern.e_commerce.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RegisterController {

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/home")
    public String home(Model model, @AuthenticationPrincipal OAuth2User oauth2User) {
        String name = oauth2User.getAttribute("name"); // Lấy tên từ Google
        model.addAttribute("name", name);
        return "home";
    }
}
