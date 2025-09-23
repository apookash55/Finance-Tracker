package dev.apookash55.finance.controller;

import dev.apookash55.finance.dto.LoginUserRequest;
import dev.apookash55.finance.dto.LoginUserResponse;
import dev.apookash55.finance.dto.RegisterUserRequest;
import dev.apookash55.finance.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public void registerUser(@Valid @RequestBody RegisterUserRequest request) {
        authService.registerUser(request);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginUserResponse> loginUser(@Valid @RequestBody LoginUserRequest request) {
        return ResponseEntity.ok(authService.loginUser(request));
    }
}
