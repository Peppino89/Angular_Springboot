package org.example.todo.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.todo.dto.*;
import org.example.todo.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest registerRequest) {
        return ResponseEntity.ok(authService.register(registerRequest));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(authService.login(loginRequest));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String>forgotPassword(@Valid @RequestBody ForgotPasswordRequest forgotPasswordRequest) {

        authService.forgotPassword(forgotPasswordRequest);
        return ResponseEntity.ok("Se l'email è registrata, riceverai un link per reimpostare la password.");
    }


    @PostMapping("/reset-password")
    public ResponseEntity<String>resetPassword(@Valid @RequestBody ResetPasswordRequest resetPasswordRequest) {
        authService.resetPassword(resetPasswordRequest);
        return ResponseEntity.ok("Password modificata con successo");
    }

    @GetMapping("/reset-password/validate")
    public ResponseEntity<ResetTokenValidationResponse>validateResetToken(@RequestParam String token) {
        return ResponseEntity.ok(authService.validateResetToken(token));
    }

}
