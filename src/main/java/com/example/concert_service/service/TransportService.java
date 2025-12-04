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
public class TransportService {

    private final SerpApiService serpApiService;
    private final VenueRepository venueRepository;

    public NearbySearchResponse getTransportOptions(Integer venueId) {
        Venue venue = venueRepository.findById(venueId)
                .orElseThrow(() -> new RuntimeException("Recinto no encontrado"));

        return serpApiService.searchNearbyTransport(
                venue.getLatitude(),
                venue.getLongitude());
    }

    public NearbySearchResponse getNearbyMetroStations(Integer venueId) {
        Venue venue = venueRepository.findById(venueId)
                .orElseThrow(() -> new RuntimeException("Recinto no encontrado"));

        return serpApiService.searchNearbyPlaces(
                venue.getLatitude(),
                venue.getLongitude(),
                "metro station",
                1000);
    }

    public NearbySearchResponse getNearbyBusStops(Integer venueId) {
        Venue venue = venueRepository.findById(venueId)
                .orElseThrow(() -> new RuntimeException("Recinto no encontrado"));

        return serpApiService.searchNearbyPlaces(
                venue.getLatitude(),
                venue.getLongitude(),
                "bus stop",
                800);
    }

    public NearbySearchResponse getNearbyTrainStations(Integer venueId) {
        Venue venue = venueRepository.findById(venueId)
                .orElseThrow(() -> new RuntimeException("Recinto no encontrado"));

        return serpApiService.searchNearbyPlaces(
                venue.getLatitude(),
                venue.getLongitude(),
                "train station",
                2000);
    }

    public PlaceInfoResponse getTransportDetails(String transportName, String location) {
        return serpApiService.getPlaceDetails(transportName, location);
    }

    public String getTransportRoute(
            BigDecimal startLat,
            BigDecimal startLng,
            Integer venueId) {

        Venue venue = venueRepository.findById(venueId)
                .orElseThrow(() -> new RuntimeException("Recinto no encontrado"));

        String route = String.format(
                "Ruta desde (%s, %s) hasta %s (%s, %s)",
                startLat, startLng,
                venue.getName(),
                venue.getLatitude(), venue.getLongitude());

        log.info("Calculando ruta: {}", route);
        return route;
    }

    public String calculateCustomRoute(
            BigDecimal startLat,
            BigDecimal startLng,
            BigDecimal endLat,
            BigDecimal endLng) {

        String route = String.format(
                "Ruta personalizada desde (%s, %s) hasta (%s, %s)",
                startLat, startLng, endLat, endLng);

        log.info("Calculando ruta personalizada: {}", route);
        return route;
    }
}
