package org.example.todo.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {

    @NotBlank(message = "Usename obbligatorio")
    private String username;

    @NotBlank(message = "Password obbligatorio")
    private String password;
}
