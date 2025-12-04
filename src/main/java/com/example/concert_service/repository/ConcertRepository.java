package com.example.concert_service.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.concert_service.model.Concert;

@Repository
public interface ConcertRepository extends JpaRepository<Concert, Integer> {

    // Buscar conciertos por estado (ej: "ACTIVO", "CANCELADO")
    List<Concert> findByStatus(String status);

    // CORREGIDO: Cambiar eventDate por dateTime
    List<Concert> findByDateTimeBetween(LocalDateTime start, LocalDateTime end);

    // Buscar conciertos después de una fecha
    List<Concert> findByDateTimeAfter(LocalDateTime dateTime);

    // Buscar conciertos por nombre (búsqueda parcial)
    List<Concert> findByNameContainingIgnoreCase(String name);

    // Buscar conciertos por venue
    List<Concert> findByVenueVenueId(Integer venueId);
}
