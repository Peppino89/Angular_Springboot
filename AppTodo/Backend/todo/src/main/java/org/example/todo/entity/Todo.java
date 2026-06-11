package org.example.todo.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.todo.enums.Priority;
import org.example.todo.enums.TodoStatus;

import java.time.LocalDateTime;

@Entity
@Table(name="todos")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Todo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String titolo;


    private String descrizione;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TodoStatus status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Priority priority;

    private LocalDateTime dataScadenza;

    private LocalDateTime dataCreazione;

    @ManyToOne
    @JoinColumn(name="user_id", nullable=false)//Foreign Key(chiave esterna)
    private User user;



}
