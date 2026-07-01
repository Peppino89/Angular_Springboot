package com.example.barberbooking.controller;

import com.example.barberbooking.dto.request.*;
import com.example.barberbooking.dto.response.*;
import com.example.barberbooking.enums.BookingStatus;
import com.example.barberbooking.service.BookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @GetMapping
    public ResponseEntity<List<BookingResponse>> getAllBookings() {
        return ResponseEntity.ok(bookingService.getAllBookings());
    }

    @GetMapping("/admin/search")
    public ResponseEntity<PageResponse<BookingResponse>> searchBookingsForAdmin(
            @RequestParam(required = false) BookingStatus status,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,

            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10")int size,
            @RequestParam(defaultValue = "appointmentDateTime") String sortBy,
            @RequestParam(defaultValue = "asc") String direction
    ){
        return ResponseEntity.ok(

                bookingService.searchBookingsForAdmin(status, username, date, page, size, sortBy, direction)
        );

    }

    @GetMapping("/user/me")
    public ResponseEntity<List<BookingResponse>> getMyBookings(@AuthenticationPrincipal UserDetails user) {
        return ResponseEntity.ok(bookingService.getMyBookings(user.getUsername()));
    }


    @GetMapping("/status/{status}")
    public ResponseEntity<List<BookingResponse>> getAllBookingsByStatus(@PathVariable BookingStatus status) {
        return ResponseEntity.ok(bookingService.getBookingsByStatus(status));
    }

    @GetMapping("/date/{date}")
    public ResponseEntity<List<BookingResponse>> getBookingsByDate(@PathVariable
                                                                       @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                                                   LocalDate date) {
            return ResponseEntity.ok(bookingService.getBooksByDate(date));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<BookingResponse> updateBookingStatus(@PathVariable Long id, @Valid @RequestBody BookingStatusRequest request, @AuthenticationPrincipal UserDetails user) {
        return ResponseEntity.ok(bookingService.updateBooking(id,request, user.getUsername()));
    }

    @PostMapping
    public ResponseEntity<BookingResponse> createBooking(@Valid @RequestBody BookingRequest bookingRequest, @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(bookingService.createBooking(bookingRequest, userDetails.getUsername()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBooking(@PathVariable Long id, @AuthenticationPrincipal UserDetails user) {
        bookingService.deleteBooking(id, user.getUsername());
        return ResponseEntity.noContent().build();
    }





}
