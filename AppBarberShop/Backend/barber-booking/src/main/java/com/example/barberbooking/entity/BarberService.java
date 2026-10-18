package com.example.barberbooking.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Entity
@Table(name="barber_service")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BarberService {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, length = 2000)
    private String description;

    private String imageUrl;

    @Positive
    @Column(nullable = false)
    private Double price;


    @Column(nullable = false)
    @Builder.Default
    private boolean active=true;


}
