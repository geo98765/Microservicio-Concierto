package com.example.concert_service.service;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.concert_service.dto.NearbyPlaceDto;
import com.example.concert_service.dto.NearbySearchResponse;
import com.example.concert_service.dto.PlaceInfoResponse;
import com.example.concert_service.mapper.VenueMapper;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class VenueServiceImpl implements VenueService {

    private final SerpApiService serpApiService;
    private final VenueMapper venueMapper;

    private static final int DEFAULT_SEARCH_RADIUS = 10000; // 10km en metros

    @Override
    @Transactional(readOnly = true)
    public Page<NearbyPlaceDto> searchVenuesInGoogleMaps(String query, Pageable pageable) {
        log.info("Buscando venues en Google Maps: {} (page: {}, size: {})",
                query, pageable.getPageNumber(), pageable.getPageSize());

        try {
            NearbySearchResponse serpResponse = serpApiService.searchVenuesByQuery(query);
            List<NearbyPlaceDto> allVenues = extractAllVenues(serpResponse);
            log.info("✅ Búsqueda completada. Total resultados: {}", allVenues.size());
            return paginateList(allVenues, pageable);

        } catch (Exception e) {
            log.error("❌ Error buscando venues en Google Maps: {}", e.getMessage(), e);
            throw new RuntimeException("Error al buscar venues en Google Maps: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<NearbyPlaceDto> searchVenuesByLocation(Double lat, Double lng, String query, Pageable pageable) {
        log.info("Buscando venues por ubicación: {},{} con query: {} (page: {}, size: {})",
                lat, lng, query, pageable.getPageNumber(), pageable.getPageSize());

        try {
            NearbySearchResponse serpResponse = serpApiService.searchNearbyPlaces(
                    BigDecimal.valueOf(lat),
                    BigDecimal.valueOf(lng),
                    query,
                    DEFAULT_SEARCH_RADIUS);

            List<NearbyPlaceDto> allVenues = extractAllVenues(serpResponse);
            log.info("✅ Búsqueda por ubicación completada. Total resultados: {}", allVenues.size());
            return paginateList(allVenues, pageable);

        } catch (Exception e) {
            log.error("❌ Error buscando venues por ubicación: {}", e.getMessage(), e);
            throw new RuntimeException("Error al buscar venues por ubicación: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public PlaceInfoResponse getVenueDetails(String query) {
        log.info("Obteniendo detalles del venue: {}", query);

        try {
            NearbySearchResponse searchResponse = serpApiService.searchVenuesByQuery(query);
            NearbyPlaceDto venue = venueMapper.extractFirstVenue(searchResponse);

            if (venue == null) {
                log.warn("⚠️  No se encontró información para: {}", query);
                throw new EntityNotFoundException("No se encontró el venue: " + query);
            }

            log.info("✅ Venue encontrado: {}", venue.getTitle());
            return venueMapper.toPlaceInfoResponseFromNearby(venue);

        } catch (EntityNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("❌ Error obteniendo detalles del venue: {}", e.getMessage(), e);
            throw new RuntimeException("Error al obtener detalles del venue: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<NearbyPlaceDto> findVenuesNearby(Double lat, Double lng, Integer radius, Pageable pageable) {
        log.info("Buscando venues cercanos a: {},{} con radio: {}m (page: {}, size: {})",
                lat, lng, radius, pageable.getPageNumber(), pageable.getPageSize());

        try {
            NearbySearchResponse serpResponse = serpApiService.searchNearbyPlaces(
                    BigDecimal.valueOf(lat),
                    BigDecimal.valueOf(lng),
                    "concert venue",
                    radius);

            List<NearbyPlaceDto> allVenues = extractAllVenues(serpResponse);
            log.info("✅ Búsqueda de venues cercanos completada. Total resultados: {}", allVenues.size());
            return paginateList(allVenues, pageable);

        } catch (Exception e) {
            log.error("❌ Error buscando venues cercanos: {}", e.getMessage(), e);
            throw new RuntimeException("Error al buscar venues cercanos: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<NearbyPlaceDto> getHotelsNearVenue(String placeId, Integer radius, Pageable pageable) {
        log.info("Obteniendo hoteles cerca del venue: {} con radio: {}m (page: {}, size: {})",
                placeId, radius, pageable.getPageNumber(), pageable.getPageSize());

        try {
            PlaceInfoResponse venueDetails = getVenueDetails(placeId);
            PlaceInfoResponse.PlaceDetail venue = getFirstPlaceDetail(venueDetails);
            validateGpsCoordinates(venue);

            NearbySearchResponse serpResponse = serpApiService.searchNearbyHotels(
                    venue.getGpsCoordinates().getLatitude(),
                    venue.getGpsCoordinates().getLongitude(),
                    radius);

            List<NearbyPlaceDto> allHotels = extractAllVenues(serpResponse);
            log.info("✅ Búsqueda de hoteles completada. Total resultados: {}", allHotels.size());
            return paginateList(allHotels, pageable);

        } catch (EntityNotFoundException | IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            log.error("❌ Error obteniendo hoteles cerca del venue: {}", e.getMessage(), e);
            throw new RuntimeException("Error al obtener hoteles: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<NearbyPlaceDto> getRestaurantsNearVenue(String placeId, Integer radius, Pageable pageable) {
        log.info("Obteniendo restaurantes cerca del venue: {} con radio: {}m (page: {}, size: {})",
                placeId, radius, pageable.getPageNumber(), pageable.getPageSize());

        try {
            PlaceInfoResponse venueDetails = getVenueDetails(placeId);
            PlaceInfoResponse.PlaceDetail venue = getFirstPlaceDetail(venueDetails);
            validateGpsCoordinates(venue);

            NearbySearchResponse serpResponse = serpApiService.searchNearbyRestaurants(
                    venue.getGpsCoordinates().getLatitude(),
                    venue.getGpsCoordinates().getLongitude(),
                    radius);

            List<NearbyPlaceDto> allRestaurants = extractAllVenues(serpResponse);
            log.info("✅ Búsqueda de restaurantes completada. Total resultados: {}", allRestaurants.size());
            return paginateList(allRestaurants, pageable);

        } catch (EntityNotFoundException | IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            log.error("❌ Error obteniendo restaurantes cerca del venue: {}", e.getMessage(), e);
            throw new RuntimeException("Error al obtener restaurantes: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<NearbyPlaceDto> getParkingNearVenue(String placeId, Pageable pageable) {
        log.info("Obteniendo estacionamientos cerca del venue: {} (page: {}, size: {})",
                placeId, pageable.getPageNumber(), pageable.getPageSize());

        try {
            PlaceInfoResponse venueDetails = getVenueDetails(placeId);
            PlaceInfoResponse.PlaceDetail venue = getFirstPlaceDetail(venueDetails);
            validateGpsCoordinates(venue);

            NearbySearchResponse serpResponse = serpApiService.searchNearbyParkings(
                    venue.getGpsCoordinates().getLatitude(),
                    venue.getGpsCoordinates().getLongitude(),
                    2000);

            List<NearbyPlaceDto> allParkings = extractAllVenues(serpResponse);
            log.info("✅ Búsqueda de estacionamientos completada. Total resultados: {}", allParkings.size());
            return paginateList(allParkings, pageable);

        } catch (EntityNotFoundException | IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            log.error("❌ Error obteniendo estacionamientos cerca del venue: {}", e.getMessage(), e);
            throw new RuntimeException("Error al obtener estacionamientos: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<NearbyPlaceDto> getTransportNearVenue(String placeId, Pageable pageable) {
        log.info("Obteniendo transporte público cerca del venue: {} (page: {}, size: {})",
                placeId, pageable.getPageNumber(), pageable.getPageSize());

        try {
            PlaceInfoResponse venueDetails = getVenueDetails(placeId);
            PlaceInfoResponse.PlaceDetail venue = getFirstPlaceDetail(venueDetails);
            validateGpsCoordinates(venue);

            NearbySearchResponse serpResponse = serpApiService.searchNearbyTransport(
                    venue.getGpsCoordinates().getLatitude(),
                    venue.getGpsCoordinates().getLongitude());

            List<NearbyPlaceDto> allTransport = extractAllVenues(serpResponse);
            log.info("✅ Búsqueda de transporte público completada. Total resultados: {}", allTransport.size());
            return paginateList(allTransport, pageable);

        } catch (EntityNotFoundException | IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            log.error("❌ Error obteniendo transporte cerca del venue: {}", e.getMessage(), e);
            throw new RuntimeException("Error al obtener transporte: " + e.getMessage(), e);
        }
    }

    private List<NearbyPlaceDto> extractAllVenues(NearbySearchResponse response) {
        if (response == null) {
            return Collections.emptyList();
        }
        if (response.getPlaceResults() != null) {
            return List.of(response.getPlaceResults());
        }
        if (response.getLocalResults() != null) {
            return response.getLocalResults();
        }
        return Collections.emptyList();
    }

    private <T> Page<T> paginateList(List<T> allItems, Pageable pageable) {
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), allItems.size());

        if (start >= allItems.size()) {
            return new PageImpl<>(Collections.emptyList(), pageable, allItems.size());
        }

        List<T> pageContent = allItems.subList(start, end);
        return new PageImpl<>(pageContent, pageable, allItems.size());
    }

    private PlaceInfoResponse.PlaceDetail getFirstPlaceDetail(PlaceInfoResponse venueDetails) {
        if (venueDetails.getLocalResults() == null || venueDetails.getLocalResults().isEmpty()) {
            throw new IllegalArgumentException("No se pudieron obtener las coordenadas del venue");
        }
        return venueDetails.getLocalResults().get(0);
    }

    private void validateGpsCoordinates(PlaceInfoResponse.PlaceDetail venue) {
        if (venue.getGpsCoordinates() == null) {
            throw new IllegalArgumentException("El venue no tiene coordenadas GPS");
        }
    }
}
