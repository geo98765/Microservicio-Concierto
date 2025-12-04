package com.example.concert_service.dto;

import java.util.List;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para respuesta de búsqueda de lugares cercanos desde SerpApi
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NearbySearchResponse {

    // Cuando hay múltiples resultados de búsqueda
    @SerializedName("local_results")
    private List<NearbyPlaceDto> localResults;

    // Cuando SerpAPI devuelve un resultado único directo
    @SerializedName("place_results")
    private NearbyPlaceDto placeResults;

    @SerializedName("search_metadata")
    private SearchMetadata searchMetadata;

    @SerializedName("search_parameters")
    private SearchParameters searchParameters;

    // Clase interna para metadata
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SearchMetadata {
        private String id;
        private String status;
        @SerializedName("created_at")
        private String createdAt;
        @SerializedName("total_time_taken")
        private Double totalTimeTaken;
    }

    // Clase interna para parámetros de búsqueda
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SearchParameters {
        private String engine;
        private String type;
        private String q;
    }
}
