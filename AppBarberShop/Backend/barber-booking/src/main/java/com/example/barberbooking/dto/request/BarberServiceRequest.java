package com.example.barberbooking.dto.request;

import jakarta.validation.constraints.*;

import lombok.Data;

@Data
public class BarberServiceRequest {

    @NotBlank(message="Il nome del servizio è obbligatorio")
    private String name;

    @NotBlank(message = "La descrizione è obbligatoria")
    private String description;

    @NotNull
    @Positive
    private Double price;

    private boolean active = true;


}
