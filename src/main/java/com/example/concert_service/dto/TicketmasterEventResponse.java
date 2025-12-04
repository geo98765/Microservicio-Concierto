package com.example.concert_service.dto;

import java.math.BigDecimal;
import java.util.List;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para respuesta de eventos de Ticketmaster
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TicketmasterEventResponse {

    @SerializedName("_embedded")
    private Embedded embedded;

    private Page page;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Embedded {
        private List<Event> events;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Event {
        private String id;
        private String name;
        private String type;
        private String url;

        private List<Image> images;

        private Sales sales;
        private Dates dates;

        @SerializedName("priceRanges")
        private List<PriceRange> priceRanges;

        @SerializedName("_embedded")
        private EventEmbedded embedded;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Image {
        private String url;
        private Integer width;
        private Integer height;
        private String ratio;
        private Boolean fallback;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Sales {
        @SerializedName("public")
        private PublicSales publicSales;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PublicSales {
        private String startDateTime;
        private String endDateTime;
        private Boolean startTBD;
        private Boolean startTBA;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Dates {
        private Start start;
        private Status status;
        private String timezone;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Start {
        private String localDate;
        private String localTime;
        private String dateTime;
        private Boolean dateTBD;
        private Boolean dateTBA;
        private Boolean timeTBA;
        private Boolean noSpecificTime;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Status {
        private String code;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PriceRange {
        private String type;
        private String currency;
        private BigDecimal min;
        private BigDecimal max;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EventEmbedded {
        private List<Venue> venues;
        private List<Attraction> attractions;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Venue {
        private String id;
        private String name;
        private String type;
        private String url;
        private String postalCode;
        private String timezone;
        private City city;
        private State state;
        private Country country;
        private Address address;
        private Location location;
        private String parkingDetail;
        private String accessibleSeatingDetail;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Attraction {
        private String id;
        private String name;
        private String type;
        private String url;
        private List<Image> images;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class City {
        private String name;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class State {
        private String name;
        private String stateCode;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Country {
        private String name;
        private String countryCode;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Address {
        private String line1;
        private String line2;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Location {
        private String longitude;
        private String latitude;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Page {
        private Integer size;
        private Integer totalElements;
        private Integer totalPages;
        private Integer number;
    }
}
