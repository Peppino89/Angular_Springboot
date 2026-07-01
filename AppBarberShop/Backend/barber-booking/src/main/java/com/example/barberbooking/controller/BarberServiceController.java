package com.example.barberbooking.controller;

import com.example.barberbooking.dto.request.BarberServiceRequest;
import com.example.barberbooking.dto.response.BarberServiceResponse;
import com.example.barberbooking.service.BarberServiceService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/services")
@CrossOrigin(origins="http://localhost:4200")
public class BarberServiceController {

    private final BarberServiceService barberServiceService;

    public BarberServiceController(BarberServiceService barberServiceService) {
        this.barberServiceService = barberServiceService;
    }

    @GetMapping
    public ResponseEntity<List<BarberServiceResponse>> getActiveServices(){
        return ResponseEntity.ok(barberServiceService.getActiveServices());
    }

    @GetMapping("/admin")
    public ResponseEntity<List<BarberServiceResponse>> getAllServices(){
        return ResponseEntity.ok(barberServiceService.getAllServices());
    }

    @PostMapping("/{id}/image")
    public ResponseEntity<BarberServiceResponse> uploadServiceImage(@PathVariable Long id, @RequestParam("file") MultipartFile file){
        return ResponseEntity.ok(barberServiceService.uploadServiceImage(id, file));
    }

    @PostMapping
    public ResponseEntity<BarberServiceResponse> createBarberService(@Valid @RequestBody BarberServiceRequest request){
        return ResponseEntity.ok(barberServiceService.createService(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BarberServiceResponse>updateBarberService(@PathVariable Long id, @Valid @RequestBody BarberServiceRequest request){
        return ResponseEntity.ok(barberServiceService.updateService(id,request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBarberService(@PathVariable Long id){
        barberServiceService.deleteService(id);
        return ResponseEntity.noContent().build();
    }



}
