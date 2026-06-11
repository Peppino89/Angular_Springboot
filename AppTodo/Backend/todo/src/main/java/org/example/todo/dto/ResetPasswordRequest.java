package org.example.todo.dto;

import jakarta.validation.constraints.*;

import lombok.Data;

@Data
public class ResetPasswordRequest {

    @NotBlank(message = "Token obbligatorio")
    private String token;

    @NotBlank(message = "Nuova password obbligatoria")
    @Size(min = 6, message = "Password minimo 6 caratteri")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!]).*$",
            message = "La password deve contenere almeno una minuscola, una maiuscola e un carattere speciale"
    )
    private String newPassword;


    @NotBlank(message = "Conferma Password")
    private String confirmPassword;
}
