package com.example.barberbooking.controller;

import com.example.barberbooking.dto.request.BarberRequest;
import com.example.barberbooking.dto.response.BarberResponse;
import com.example.barberbooking.service.BarberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/barbers")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class BarberController {
    private final BarberService barberService;

    @GetMapping
    public ResponseEntity<List<BarberResponse>> getActiveBarbers(){
        return ResponseEntity.ok(barberService.getActiveBarbers());
    }

    @GetMapping("/{barberId}")
    public ResponseEntity<BarberResponse>getBarberById(@PathVariable("barberId") Long barberId){
        return ResponseEntity.ok(barberService.getBarberById(barberId));
    }

    @PostMapping
    public ResponseEntity<BarberResponse> createBarber(@Valid @RequestBody BarberRequest barberRequest){
        return ResponseEntity.ok(barberService.createBarber(barberRequest));
    }

    @PutMapping("/{barberId}")
    public ResponseEntity<BarberResponse> updateBarber(@PathVariable Long barberId, @Valid @RequestBody BarberRequest barberRequest){
        return ResponseEntity.ok(barberService.updateBarber(barberId, barberRequest));
    }

    @DeleteMapping("/{barberId}")
    public ResponseEntity<Void> deleteBarber(@PathVariable Long barberId){
        barberService.deleteBarber(barberId);
        return ResponseEntity.noContent().build();
    }


    @PostMapping("/{barberId}/image")
    public ResponseEntity<BarberResponse> uploadBarberImage(@PathVariable Long barberId, @RequestParam("file") MultipartFile file){
        return ResponseEntity.ok(barberService.uploadBarberImage(barberId, file));
    }


}
