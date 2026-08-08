package com.example.barberbooking.service;

import com.example.barberbooking.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service("bookingAuthorization")
@RequiredArgsConstructor
public class BookingAuthorizationService {

    private final BookingRepository bookingRepository;

    // Usato da @PreAuthorize per verificare che la prenotazione appartenga all'utente autenticato.
    public boolean isOwner(Long bookingId, String username) {
        return bookingRepository.findById(bookingId)
                .map(booking -> booking.getUser().getUsername().equals(username))
                .orElse(false);
    }
}
