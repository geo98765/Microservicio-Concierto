package com.example.concert_service.dto;

import java.math.BigDecimal;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para información de un evento/concierto encontrado
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArtistEventInfo {

    // Información del evento
    private String eventName;
    private String eventDate;
    private String eventType;
    private String ticketUrl;
    private String ticketPrice;
    private String status;

    // Información del venue
    private VenueInfo venue;

    // Información adicional del lugar
    private WeatherResponse weather;
    private List<NearbyPlaceDto> nearbyHotels;
    private List<NearbyPlaceDto> nearbyTransport;

    /**
     * Información del venue/recinto
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class VenueInfo {
        private String name;
        private String address;
        private BigDecimal latitude;
        private BigDecimal longitude;
        private String placeId;
        private BigDecimal rating;
        private Integer reviews;
        private String phone;
        private String website;
        private String parkingInfo;
        private String accessibilityInfo;
        private String timezone;
    }
}
