package com.example.barberbooking.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {

   @NotBlank(message = "Username obbligatorio")
    private String username;

   @NotBlank(message = "Password Obbligatoria")
    private String password;
}
