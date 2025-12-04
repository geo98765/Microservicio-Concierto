package com.example.concert_service.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.concert_service.model.Venue;

@Repository
public interface VenueRepository extends JpaRepository<Venue, Integer> {

    List<Venue> findByCity(String city);

    // NUEVO: Buscar por nombre (búsqueda parcial, case-insensitive)
    List<Venue> findByNameContainingIgnoreCase(String name);

    @Query("SELECT v FROM Venue v WHERE " +
            "(6371 * acos(cos(radians(:latitude)) * cos(radians(v.latitude)) * " +
            "cos(radians(v.longitude) - radians(:longitude)) + " +
            "sin(radians(:latitude)) * sin(radians(v.latitude)))) <= :radius")
    List<Venue> findVenuesWithinRadius(
            @Param("latitude") BigDecimal latitude,
            @Param("longitude") BigDecimal longitude,
            @Param("radius") BigDecimal radius);
}
