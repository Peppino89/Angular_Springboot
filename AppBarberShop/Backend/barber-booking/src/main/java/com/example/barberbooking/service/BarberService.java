package com.example.barberbooking.service;

import com.example.barberbooking.dto.request.BarberRequest;
import com.example.barberbooking.dto.response.BarberResponse;
import com.example.barberbooking.entity.Barber;
import com.example.barberbooking.exception.BarberNotFoundException;
import com.example.barberbooking.repository.BarberRepository;
import com.example.barberbooking.storage.FileStorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BarberService {

    private final BarberRepository barberRepository;
    private final FileStorageService fileStorageService;


     public List<BarberResponse> getActiveBarbers(){
         return barberRepository.findByActiveTrueOrderByNameAsc()
                 .stream()
                 .map(this::toResponse)
                 .toList();
     }


     public BarberResponse getBarberById(Long barberId){
         return toResponse(findBarberById(barberId));
     }

    @PreAuthorize("hasRole('ADMIN')")
     public BarberResponse createBarber(BarberRequest barberRequest){
         Barber barber = Barber.builder()
                 .name(barberRequest.getName())
                 .bio(barberRequest.getBio())
                 .active(barberRequest.isActive())
                 .onVacation(barberRequest.isOnVacation())
                 .build();

         return toResponse(barberRepository.save(barber));
     }

    @PreAuthorize("hasRole('ADMIN')")
     public BarberResponse updateBarber(Long barberId, BarberRequest barberRequest){
         Barber existingBarber = findBarberById(barberId);

         existingBarber.setName(barberRequest.getName());
         existingBarber.setBio(barberRequest.getBio());
         existingBarber.setActive(barberRequest.isActive());
         existingBarber.setOnVacation(barberRequest.isOnVacation());

         return toResponse(barberRepository.save(existingBarber));

     }

    @PreAuthorize("hasRole('ADMIN')")
     public void deleteBarber(Long barberId){
         Barber barber = findBarberById(barberId);
         barber.setActive(false);
         barberRepository.save(barber);

     }

    @PreAuthorize("hasRole('ADMIN')")
     public BarberResponse uploadBarberImage(Long barberId, MultipartFile file){

         Barber barber = findBarberById(barberId);

         deleteImageIfExists(barber);

         String imageUrl = fileStorageService.upload(file);
         barber.setImageUrl(imageUrl);

         return toResponse(barberRepository.save(barber));

     }


     private void deleteImageIfExists(Barber barber){
         if(barber.getImageUrl() != null && !barber.getImageUrl().isBlank()){
             log.info("Eliminazione immagine associata al barbiere con id {}", barber.getId());
             fileStorageService.delete(barber.getImageUrl());
         }
     }

     private BarberResponse toResponse(Barber barber){
         BarberResponse response = new BarberResponse();

         response.setId(barber.getId());
         response.setName(barber.getName());
         response.setBio(barber.getBio());
         response.setImageUrl(barber.getImageUrl());
         response.setActive(barber.isActive());
         response.setOnVacation(barber.isOnVacation());

         return response;

     }

     private Barber findBarberById(Long barberId){
         return barberRepository.findById(barberId)
                 .orElseThrow(()->new BarberNotFoundException("Barbiere non trovato"));
     }
}
