package com.example.barberbooking.dto.request;

import jakarta.validation.constraints.*;

import lombok.Data;

@Data
public class ResetPasswordRequest {

    @NotBlank(message = "Token obbligatorio")
    private String token;

    @NotBlank(message="Password obbligatoria")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!]).{6,}$",
            message = "La password deve contenere almeno 6 caratteri, una minuscola, una maiuscola e un carattere speciale"
    )
    private String newPassword;

    @NotBlank(message = "Conferma password obbligatoria")
    private String confirmNewPassword;

}
