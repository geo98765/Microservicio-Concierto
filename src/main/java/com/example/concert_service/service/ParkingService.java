package com.example.concert_service.service;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.example.concert_service.dto.NearbySearchResponse;
import com.example.concert_service.dto.PlaceInfoResponse;
import com.example.concert_service.model.Venue;
import com.example.concert_service.repository.VenueRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ParkingService {

    private final SerpApiService serpApiService;
    private final VenueRepository venueRepository;

    public NearbySearchResponse getParkingsNearVenue(Integer venueId) {
        Venue venue = venueRepository.findById(venueId)
                .orElseThrow(() -> new RuntimeException("Recinto no encontrado"));

        return serpApiService.searchNearbyParkings(
                venue.getLatitude(),
                venue.getLongitude(),
                2000 // 2 km de radio
        );
    }

    public NearbySearchResponse getParkingsWithinRadius(Integer venueId, Integer radiusMeters) {
        Venue venue = venueRepository.findById(venueId)
                .orElseThrow(() -> new RuntimeException("Recinto no encontrado"));

        return serpApiService.searchNearbyParkings(
                venue.getLatitude(),
                venue.getLongitude(),
                radiusMeters);
    }

    public PlaceInfoResponse getParkingDetails(String parkingName, String location) {
        return serpApiService.getPlaceDetails(parkingName, location);
    }

    public NearbySearchResponse filterParkingsByPrice(Integer venueId, BigDecimal maxPrice) {
        NearbySearchResponse parkings = getParkingsNearVenue(venueId);

        // TODO: Implementar filtrado por precio
        // Necesitarás parsear la información de precios de las descripciones
        log.info("Filtrando estacionamientos con precio máximo: {}", maxPrice);

        return parkings;
    }

    public NearbySearchResponse getFreeParkings(Integer venueId) {
        Venue venue = venueRepository.findById(venueId)
                .orElseThrow(() -> new RuntimeException("Recinto no encontrado"));

        return serpApiService.searchNearbyPlaces(
                venue.getLatitude(),
                venue.getLongitude(),
                "free parking",
                2000);
    }

    public NearbySearchResponse getCoveredParkings(Integer venueId) {
        Venue venue = venueRepository.findById(venueId)
                .orElseThrow(() -> new RuntimeException("Recinto no encontrado"));

        return serpApiService.searchNearbyPlaces(
                venue.getLatitude(),
                venue.getLongitude(),
                "covered parking garage",
                2000);
    }
}
