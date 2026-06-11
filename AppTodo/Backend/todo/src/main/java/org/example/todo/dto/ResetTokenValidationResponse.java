package org.example.todo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ResetTokenValidationResponse {

    private String message;
    private LocalDateTime expiresAt;
}
