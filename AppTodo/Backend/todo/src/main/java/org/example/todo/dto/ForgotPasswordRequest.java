package org.example.todo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ForgotPasswordRequest {

    @NotBlank(message = "Email obbligatoria")
    @Email(message = "Email non valida")
    private String email;

}
