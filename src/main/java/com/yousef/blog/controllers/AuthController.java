package com.yousef.blog.controllers;

import com.yousef.blog.domain.dtos.AuthResponse;
import com.yousef.blog.domain.dtos.LoginRequest;
import com.yousef.blog.services.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationService authService;

    @GetMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        var userDetails = authService.authenticate(loginRequest.email(), loginRequest.password());

        return ResponseEntity.ok(
                AuthResponse.builder()
                        .token(authService.generateToken(userDetails))
                        .expiresIn(8640L)
                        .build()
        );
    }
}
