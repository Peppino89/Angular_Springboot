package com.example.barberbooking.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BookingRequest {

    @NotNull(message = "Data e ora sono obbligatorie")
    @Future(message = "La data dell' appuntamento deve essere futura")
    private LocalDateTime appointmentDateTime;

    @NotNull(message = "Servizio obbligatorio")
    private Long barberServiceId;

}
