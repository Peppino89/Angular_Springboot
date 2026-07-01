package com.example.barberbooking.dto.response;

import lombok.*;

@Data
@AllArgsConstructor
public class AuthResponse {
    private String token;
    private String username;
    private String email;
}
