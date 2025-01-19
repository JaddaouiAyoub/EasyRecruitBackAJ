package org.example.easyrecruitbackend.controller;

import lombok.RequiredArgsConstructor;
import org.example.easyrecruitbackend.dto.LoginRequest;
import org.example.easyrecruitbackend.service.KeycloakAuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
//@CrossOrigin("*")
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final KeycloakAuthService keycloakAuthService;

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(
                keycloakAuthService.login(loginRequest.getUsername(), loginRequest.getPassword())
        );
    }

    @PostMapping("/refresh")
    public ResponseEntity<Map<String, String>> refresh(@RequestParam String refreshToken) {
        return ResponseEntity.ok(keycloakAuthService.refreshAccessToken(refreshToken));
    }
}

