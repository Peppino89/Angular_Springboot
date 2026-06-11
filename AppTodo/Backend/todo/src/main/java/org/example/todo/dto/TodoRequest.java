package org.example.todo.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.example.todo.enums.*;

import java.time.LocalDateTime;

@Data
public class TodoRequest {
    @NotBlank(message = "Titolo obbligatorio")
    private String titolo;

    private String descrizione;

    @NotNull(message = "Status obbligatorio")
    private TodoStatus status;

    @NotNull(message = "Priorità obbligatoria")
    private Priority priority;

    private LocalDateTime dataScadenza;

}
