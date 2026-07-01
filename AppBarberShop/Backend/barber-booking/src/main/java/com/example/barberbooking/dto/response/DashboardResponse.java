package com.example.barberbooking.dto.response;

import lombok.*;

@Data
@AllArgsConstructor
public class DashboardResponse {
    private long totalBookings;
    private long pendingBookings;
    private long confirmedBookings;
    private long cancelledBookings;

}
