package com.example.barberbooking.repository;

import com.example.barberbooking.entity.Booking;
import com.example.barberbooking.enums.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long>, JpaSpecificationExecutor<Booking> {

    List<Booking> findByUserId(Long userId);

    List<Booking> findByStatus(BookingStatus status);
    boolean existsByAppointmentDateTimeAndBarberServiceId(LocalDateTime appointmentDataTime, Long barberServiceId);
    List<Booking> findByAppointmentDateTimeBetween(LocalDateTime start, LocalDateTime end);
    Long countByStatus(BookingStatus status);

}
