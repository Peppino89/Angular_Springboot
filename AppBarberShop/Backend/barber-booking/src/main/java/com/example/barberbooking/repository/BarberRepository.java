package com.example.barberbooking.repository;

import com.example.barberbooking.entity.Barber;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BarberRepository extends JpaRepository<Barber, Long> {
    List<Barber> findByActiveTrueOrderByNameAsc();
    List<Barber> findByActiveTrueOrderByIdAsc();

    @Lock(LockModeType.PESSIMISTIC_WRITE)//applica un lock esclusivo sulla riga del barbiere
    @Query("SELECT b FROM Barber b WHERE b.id = :barberId")
    Optional<Barber> findByIdForUpdate(@Param("barberId")Long barberId);
}