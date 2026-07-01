package com.example.barberbooking.repository;

import com.example.barberbooking.entity.BarberService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BarberServiceRepository extends JpaRepository<BarberService,Long> {

    List<BarberService> findByActiveTrue();


}
