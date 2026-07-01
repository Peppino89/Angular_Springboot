package com.example.barberbooking.service;

import com.example.barberbooking.dto.request.BarberServiceRequest;
import com.example.barberbooking.dto.response.BarberServiceResponse;
import com.example.barberbooking.entity.BarberService;
import com.example.barberbooking.exception.BarberServiceNotFoundException;
import com.example.barberbooking.repository.*;
import com.example.barberbooking.storage.FileStorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class BarberServiceService {

    private final BarberServiceRepository barberServiceRepository;
    private final UserRepository userRepository;
    private final FileStorageService fileStorageService;



    public List<BarberServiceResponse> getAllServices() {
        return barberServiceRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<BarberServiceResponse> getActiveServices() {
        return barberServiceRepository.findByActiveTrue()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public BarberServiceResponse createService(BarberServiceRequest request) {

        BarberService barberService = BarberService.builder()
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .durationMinutes(request.getDurationMinutes())
                .active(request.isActive())
                .build();

        log.info("Creazione nuovo servizio: {}", request.getName());
        return toResponse(barberServiceRepository.save(barberService));

    }

    public BarberServiceResponse updateService(Long id,BarberServiceRequest request) {

        BarberService existingBarberService = barberServiceRepository.findById(id)
                .orElseThrow(()->new BarberServiceNotFoundException("Servizio non trovato"));

        existingBarberService.setName(request.getName());
        existingBarberService.setDescription(request.getDescription());
        existingBarberService.setPrice(request.getPrice());
        existingBarberService.setDurationMinutes(request.getDurationMinutes());
        existingBarberService.setActive(request.isActive());

        log.info("Aggiornamento servizio con id {}", id);
        return toResponse(barberServiceRepository.save(existingBarberService));

    }

    public void deleteService(Long id){
        BarberService barberService = barberServiceRepository.findById(id)
                .orElseThrow(()->new BarberServiceNotFoundException("Servizio non trovato"));

        log.info("Avvio eliminazione del servizio con id {}", id);

        deleteImageIfExists(barberService);

        barberServiceRepository.delete(barberService);

        log.info("Servizio con id {} eliminato correttamente", id);

    }


    public BarberServiceResponse uploadServiceImage(Long serviceId, MultipartFile file) {

        BarberService barberService = barberServiceRepository.findById(serviceId)
                .orElseThrow(()->new BarberServiceNotFoundException("Servizio non trovato"));

          deleteImageIfExists(barberService);

        String imageUrl = fileStorageService.upload(file);
        barberService.setImageUrl(imageUrl);

        return toResponse(barberServiceRepository.save(barberService));
    }


    private BarberServiceResponse toResponse(BarberService barberService) {
        BarberServiceResponse barberServiceResponse = new BarberServiceResponse();

        barberServiceResponse.setId(barberService.getId());
        barberServiceResponse.setName(barberService.getName());
        barberServiceResponse.setDescription(barberService.getDescription());
        barberServiceResponse.setImageUrl(barberService.getImageUrl());
        barberServiceResponse.setPrice(barberService.getPrice());
        barberServiceResponse.setDurationMinutes(barberService.getDurationMinutes());
        barberServiceResponse.setActive(barberService.isActive());

        return barberServiceResponse;
    }

    private void deleteImageIfExists(BarberService  barberService) {
        if(barberService.getImageUrl() != null && !barberService.getImageUrl().isBlank()){

            log.info("Eliminazione immagine associata al servizio con id {}", barberService.getId());

            fileStorageService.delete(barberService.getImageUrl());
        }
    }

}
