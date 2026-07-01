package com.example.barberbooking.dto.response;

import com.example.barberbooking.enums.BookingStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BookingResponse {

    private Long id;
    private LocalDateTime appointmentDateTime;
    private BookingStatus status;
    private Long userId;
    private String username;
    private Long barberServiceId;
    private String serviceName;
}
