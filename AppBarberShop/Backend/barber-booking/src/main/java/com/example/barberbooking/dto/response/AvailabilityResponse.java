package com.example.barberbooking.dto.response;


import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
public class AvailabilityResponse {

    private Long barberId;
    private LocalDate date;
    private boolean workingDay;
    private boolean onVacation;
    private boolean complete;
    private List<LocalTime> availableSlots;

}
