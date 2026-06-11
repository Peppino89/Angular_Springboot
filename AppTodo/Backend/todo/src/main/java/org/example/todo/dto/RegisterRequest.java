package org.example.todo.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class RegisterRequest {

    @NotBlank(message = "Username obbligatoria")
    @Size(min=3, message = "L' Username deve contenere almeno 3 caratteri")
    private String username;

    @NotBlank(message = "Email obbligatoria")
    @Email(message = "Email non valida")
    private String email;

    @NotBlank(message = "Password obbligatoria")
    @Size(min=6,message = "Password  minimo 6 carratteri")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!]).*$",
            message = "La password deve contenere almeno una minuscola, una maiuscola e un carattere speciale"
    )
    private String password;


}
