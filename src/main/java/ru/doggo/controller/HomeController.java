package ru.doggo.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/")
    public String home(Authentication authentication) {
        System.out.println(authentication.getName());
        System.out.println(authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList());
        return "Hello, world!";
    }

    @GetMapping("/user")
    public String user() {
        return "Hello User";
    }

    @GetMapping("/admin")
    public String admin() {
        return "Hello Admin";
    }
}
