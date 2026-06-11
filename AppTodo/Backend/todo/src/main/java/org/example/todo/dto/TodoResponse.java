package org.example.todo.dto;

import lombok.Data;
import org.example.todo.enums.*;

import java.time.LocalDateTime;

@Data
public class TodoResponse {

    private Long id;
    private String titolo;
    private String descrizione;
    private TodoStatus status;
    private Priority priority;
    private LocalDateTime dataScadenza;
    private String dataCreazione;
    private String username;

}
