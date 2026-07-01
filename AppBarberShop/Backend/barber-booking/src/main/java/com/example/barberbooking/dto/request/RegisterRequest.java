package com.example.barberbooking.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class RegisterRequest {

    @NotBlank(message = "Username obbligatorio")
    @Size(min=4,message = "La username deve contenere almeno 4 caratteri")
    private String username;


    @NotBlank(message = "Email obbligatoria")
    @Email(message = "Email non valida")
    private String email;

    @NotBlank(message = "Password obbligatoria")
    //@Size(min=6,message = "Password  minimo 6 caratteri")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!]).{6,}$",
            message = "La password deve contenere almeno 6 caratteri, una minuscola, una maiuscola e un carattere speciale"
    )
    private String password;
}
