package com.example.barberbooking.dto.response;

import lombok.Data;

@Data
public class BarberServiceResponse {
    private Long id;
    private String name;
    private String description;
    private String imageUrl;
    private Double price;
    private Integer durationMinutes;
    private boolean active;
}
