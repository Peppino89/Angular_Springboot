package com.example.barberbooking.dto.response;

import lombok.Data;

@Data
public class BarberResponse {
    private Long id;
    private String name;
    private String bio;
    private String imageUrl;
    private boolean active;
    private boolean onVacation;
}
