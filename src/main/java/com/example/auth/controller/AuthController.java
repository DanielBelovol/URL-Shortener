package com.example.auth.controller;

import com.example.auth.service.AuthService;
import com.example.auth.dto.AuthRequest;
import com.example.auth.dto.AuthResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "Auth", description = "The Auth API")
@RequestMapping("/api/V1/auth")
public class AuthController {

    private static final String REGISTRATION_MESSAGE = "User registered successfully!";
    private static final String LOGIN_MESSAGE = "User logged in successfully!";

    private final AuthService authService;

    @Operation(summary = "Log In")
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody @Valid AuthRequest authRequest) {
        String token = authService.loginUser(authRequest);
        return ResponseEntity.ok(new AuthResponse(LOGIN_MESSAGE, token));
    }

    @Operation(summary = "Registration")
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody @Valid AuthRequest authRequest) {
        String token = authService.registerUser(authRequest);
        return ResponseEntity.ok(new AuthResponse(REGISTRATION_MESSAGE, token));
    }


}
