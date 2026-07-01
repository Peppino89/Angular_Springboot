package com.example.barberbooking.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ResetTokenValidationResponse {

    private boolean valid;
    private String message;
    private LocalDateTime expiresAt;

}
