package com.example.barberbooking.dto.response;

import com.example.barberbooking.enums.Role;
import lombok.*;

@Data
@AllArgsConstructor
public class AuthResponse {
    private String token;
    private String username;
    private String email;
    private Role role;
}
