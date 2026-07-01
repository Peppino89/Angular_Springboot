package com.example.barberbooking.service;

import com.example.barberbooking.dto.request.*;
import com.example.barberbooking.dto.*;
import com.example.barberbooking.dto.response.BookingResponse;
import com.example.barberbooking.dto.response.PageResponse;
import com.example.barberbooking.entity.BarberService;
import com.example.barberbooking.entity.Booking;
import com.example.barberbooking.entity.User;
import com.example.barberbooking.enums.BookingStatus;
import com.example.barberbooking.exception.*;
import com.example.barberbooking.repository.BarberServiceRepository;
import com.example.barberbooking.repository.BookingRepository;
import com.example.barberbooking.repository.UserRepository;
import com.example.barberbooking.specification.BookingSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final BarberServiceRepository barberServiceRepository;
    private final UserRepository userRepository;

    public BookingResponse createBooking(BookingRequest request, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("Utente non trovato"));

        BarberService barberService = barberServiceRepository.findById(request.getBarberServiceId())
                .orElseThrow(()->new BarberServiceNotFoundException("Servizio non trovato"));


        boolean alreadyExists = bookingRepository.existsByAppointmentDateTimeAndBarberServiceId(request.getAppointmentDateTime(),
                request.getBarberServiceId());

        if(alreadyExists){
            throw new BookingAlreadyExistsException(
                    "Esiste già una prenotazione per questo servizio nella data e ora selezionata"
            );
        }

        Booking booking = Booking.builder()
                .appointmentDateTime(request.getAppointmentDateTime())
                .status(BookingStatus.IN_ATTESA)
                .user(user)
                .barberService(barberService)
                .build();

        return toResponse(bookingRepository.save(booking));

    }


    public List<BookingResponse> getAllBookings() {
        return bookingRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<BookingResponse> getMyBookings(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(()->new UserNotFoundException("Utente non trovato"));

        return bookingRepository.findByUserId(user.getId())
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<BookingResponse> getBookingsByStatus(BookingStatus status) {
        return bookingRepository.findByStatus(status)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public BookingResponse updateBooking(Long id, BookingStatusRequest request, String username) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(()->new BookingNotFoundException("Prenotazione non trovata"));


        if(!booking.getUser().getUsername().equals(username)){
            throw new UnauthorizedException("Non sei autorizzato a modificare questa prenotazione");
        }

           booking.setStatus(request.getStatus());

           return toResponse(bookingRepository.save(booking));
    }

    public void deleteBooking(Long id, String username) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(()->new BookingNotFoundException("Prenotazione non trovata"));
        if(!booking.getUser().getUsername().equals(username)){
            throw new UnauthorizedException("Non sei autorizzato a eliminare questa prenotazione");
        }

        bookingRepository.delete(booking);
    }


    public List<BookingResponse>getBooksByDate(LocalDate date) {
        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.plusDays(1).atStartOfDay();

        return bookingRepository.findByAppointmentDateTimeBetween(start,end)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public PageResponse<BookingResponse> searchBookingsForAdmin(
            BookingStatus status,
            String username,
            LocalDate date,
            int page,
            int size,
            String sortBy,
            String direction
    ){

        Sort sort = direction.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();

        Pageable pegeable = PageRequest.of(page, size,sort);

        Specification<Booking> specification = BookingSpecification.hasStatus(status)
                .and(BookingSpecification.hasUsername(username))
                .and(BookingSpecification.hasDate(date));

        Page<Booking> bookingPage = bookingRepository.findAll(specification, pegeable);

        List<BookingResponse> content = bookingPage.getContent()
                .stream()
                .map(this::toResponse)
                .toList();

        return new PageResponse<>(
                content,
                bookingPage.getNumber(),
                bookingPage.getSize(),
                bookingPage.getTotalElements(),
                bookingPage.getTotalPages(),
                bookingPage.isLast()
        );


    }

    private  BookingResponse toResponse(Booking booking) {
        BookingResponse bookingResponse = new BookingResponse();

        bookingResponse.setId(booking.getId());
        bookingResponse.setAppointmentDateTime(booking.getAppointmentDateTime());
        bookingResponse.setStatus(booking.getStatus());

        bookingResponse.setUserId(booking.getUser().getId());
        bookingResponse.setUsername(booking.getUser().getUsername());

        bookingResponse.setBarberServiceId(booking.getBarberService().getId());
        bookingResponse.setServiceName(booking.getBarberService().getName());

        return bookingResponse;
    }

}
