package com.example.barberbooking.service;

import com.example.barberbooking.dto.response.DashboardResponse;
import com.example.barberbooking.enums.BookingStatus;
import com.example.barberbooking.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final BookingRepository bookingRepository;

    public DashboardResponse getStats(){

        long totalBookings = bookingRepository.count();

        long pendingBookings = bookingRepository.countByStatus(BookingStatus.IN_ATTESA);

        long confirmedBookings = bookingRepository.countByStatus(BookingStatus.CONFERMATA);

        long cancelledBookings = bookingRepository.countByStatus(BookingStatus.ANNULLATA);

        return  new DashboardResponse(totalBookings,
                                      pendingBookings,
                                      confirmedBookings,
                                      cancelledBookings);

    }

}
