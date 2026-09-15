package com.neoblishange.portfolio.controller;

import com.neoblishange.portfolio.dto.auth.LoginRequestDTO;
import com.neoblishange.portfolio.dto.auth.LoginResponseDTO;
import com.neoblishange.portfolio.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(
            @Valid @RequestBody LoginRequestDTO request) {

        String token = authService.login(request);

        return ResponseEntity.ok(new LoginResponseDTO(token));
    }
}