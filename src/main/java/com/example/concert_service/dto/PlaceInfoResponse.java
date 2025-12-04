package com.example.concert_service.dto;

import java.math.BigDecimal;
import java.util.List;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para información detallada de un lugar (hotel, restaurante, parking,
 * etc.)
 * desde SerpApi Google Maps
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlaceInfoResponse {

    @SerializedName("local_results")
    private List<PlaceDetail> localResults;

    @SerializedName("search_metadata")
    private SearchMetadata searchMetadata;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PlaceDetail {
        private String title;

        @SerializedName("place_id")
        private String placeId;

        private String address;
        private BigDecimal rating;
        private Integer reviews;
        private String phone;

        @SerializedName("phone_number")
        private String phoneNumber;

        private String website;
        private String description;
        private List<String> types;

        @SerializedName("type_id")
        private String typeId;

        private String price;

        @SerializedName("price_level")
        private String priceLevel;

        @SerializedName("gps_coordinates")
        private GpsCoordinates gpsCoordinates;

        private List<String> amenities;

        @SerializedName("opening_hours")
        private OpeningHoursDto openingHours;

        private List<ReviewDto> reviews_list;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GpsCoordinates {
        private BigDecimal latitude;
        private BigDecimal longitude;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SearchMetadata {
        private String status;
    }
}
