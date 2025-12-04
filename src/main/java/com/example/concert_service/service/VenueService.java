package com.example.concert_service.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.concert_service.dto.NearbyPlaceDto;
import com.example.concert_service.dto.PlaceInfoResponse;

public interface VenueService {

    Page<NearbyPlaceDto> searchVenuesInGoogleMaps(String query, Pageable pageable);

    Page<NearbyPlaceDto> searchVenuesByLocation(Double lat, Double lng, String query, Pageable pageable);

    PlaceInfoResponse getVenueDetails(String query);

    Page<NearbyPlaceDto> findVenuesNearby(Double lat, Double lng, Integer radius, Pageable pageable);

    Page<NearbyPlaceDto> getHotelsNearVenue(String placeId, Integer radius, Pageable pageable);

    Page<NearbyPlaceDto> getRestaurantsNearVenue(String placeId, Integer radius, Pageable pageable);

    Page<NearbyPlaceDto> getParkingNearVenue(String placeId, Pageable pageable);

    Page<NearbyPlaceDto> getTransportNearVenue(String placeId, Pageable pageable);
}
