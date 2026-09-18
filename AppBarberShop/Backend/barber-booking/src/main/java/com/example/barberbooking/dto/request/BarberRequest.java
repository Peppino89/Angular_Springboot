package com.example.barberbooking.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class BarberRequest {

    @NotBlank(message = "Il nome è obbligatorio")
    @Size(min = 3,max = 100, message = "Il nome deve contenere da 3 a 100 caratteri")
    private String name;

    @Size(
            max = 2000,
            message = "La biografia non può superare 2000 caratteri"
    )
    private String bio;
    private boolean active = true;
    private boolean onVacation = false;
}
