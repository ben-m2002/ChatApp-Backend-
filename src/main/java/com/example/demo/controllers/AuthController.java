package com.example.demo.controllers;


import com.example.demo.dtos.AuthRequestDto;
import com.example.demo.dtos.AuthResponseDto;
import com.example.demo.dtos.RegisterRequestDto;
import com.example.demo.dtos.UsersResponseDto;
import com.example.demo.services.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDto> register(@RequestBody RegisterRequestDto registerRequestDto) {
        return authService.register(registerRequestDto);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@RequestBody AuthRequestDto authRequestDto) {
        return authService.login(authRequestDto);
    }

    @GetMapping("/users")
    public ResponseEntity<UsersResponseDto> getUsers(HttpServletRequest request) {
        return authService.getUsers(request);
    }

}
