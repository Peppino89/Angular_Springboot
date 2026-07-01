package com.example.barberbooking.dto.request;

import com.example.barberbooking.enums.BookingStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BookingStatusRequest {
    @NotNull
    private BookingStatus status;
}
