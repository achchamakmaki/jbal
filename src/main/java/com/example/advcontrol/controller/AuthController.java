package com.example.advcontrol.controller;

import com.example.advcontrol.dto.LoginRequest;
import com.example.advcontrol.dto.LoginResponse;
import com.example.advcontrol.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final JwtService jwtService;

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {

        String username = request.getUsername();
        String password = request.getPassword();

        if ("adv".equals(username) && "1234".equals(password)) {
            String token = jwtService.generateToken(username, "ADV");
            return new LoginResponse(token, username, "ADV");
        }

        if ("manager".equals(username) && "1234".equals(password)) {
            String token = jwtService.generateToken(username, "MANAGER");
            return new LoginResponse(token, username, "MANAGER");
        }

        if ("dg".equals(username) && "1234".equals(password)) {
            String token = jwtService.generateToken(username, "DG");
            return new LoginResponse(token, username, "DG");
        }

        throw new RuntimeException("Identifiants invalides");
    }
}
