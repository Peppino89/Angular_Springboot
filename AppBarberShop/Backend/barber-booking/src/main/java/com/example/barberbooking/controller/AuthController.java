package com.example.barberbooking.controller;

import com.example.barberbooking.dto.request.*;
import com.example.barberbooking.dto.response.*;
import com.example.barberbooking.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request){
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request){

        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request){
        authService.forgotPassword(request);
        return ResponseEntity.ok("Se l'email è registrata, riceverai un link per reimpostare la password");
    }

   @GetMapping("/reset-password/validate")
    public ResponseEntity<ResetTokenValidationResponse> validateToken(@RequestParam String token){
        return ResponseEntity.ok(authService.validateToken(token));
   }

   @PostMapping("/reset-password")
    public ResponseEntity<String>resetPassword(@Valid @RequestBody ResetPasswordRequest request){
        authService.resetPassword(request);
      return   ResponseEntity.ok("Password aggiornata con successo");
   }

}
