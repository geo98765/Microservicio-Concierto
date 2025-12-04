package com.example.concert_service.mapper;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.example.concert_service.dto.PlaceInfoResponse;
import com.example.concert_service.dto.NearbyPlaceDto;
import com.example.concert_service.dto.NearbySearchResponse;

/**
 * Mapper para conversiones de DTOs relacionados con Venues
 * Este mapper SOLO hace conversiones simples de DTOs
 * La lógica de negocio pertenece a la capa Service
 */
@Component
public class VenueMapper {

    /**
     * Convierte NearbyPlaceDto a PlaceInfoResponse.PlaceDetail
     * Mapea información básica de un lugar desde SerpApi a formato de respuesta
     * detallada
     * 
     * @param nearbyPlace DTO de SerpApi con información del lugar
     * @return PlaceDetail con información completa del lugar
     */
    public PlaceInfoResponse.PlaceDetail toPlaceDetail(NearbyPlaceDto nearbyPlace) {
        if (nearbyPlace == null) {
            return null;
        }

        return PlaceInfoResponse.PlaceDetail.builder()
                .title(nearbyPlace.getTitle())
                .placeId(nearbyPlace.getPlaceId())
                .address(nearbyPlace.getAddress())
                .rating(nearbyPlace.getRating())
                .reviews(nearbyPlace.getReviews())
                .phone(nearbyPlace.getPhone())
                .phoneNumber(nearbyPlace.getPhone())
                .website(nearbyPlace.getWebsite())
                .description(nearbyPlace.getDescription())
                .types(nearbyPlace.getTypes())
                .typeId(nearbyPlace.getTypeId())
                .price(nearbyPlace.getPrice())
                .priceLevel(nearbyPlace.getPrice())
                .gpsCoordinates(toGpsCoordinates(nearbyPlace.getGpsCoordinates()))
                .amenities(nearbyPlace.getAmenities())
                .build();
    }

    /**
     * Convierte coordenadas GPS de NearbyPlaceDto a PlaceInfoResponse
     * Mapea las coordenadas geográficas entre diferentes estructuras de DTOs
     * 
     * @param gpsCoords Coordenadas GPS del DTO de SerpApi
     * @return Coordenadas GPS en formato PlaceInfoResponse
     */
    public PlaceInfoResponse.GpsCoordinates toGpsCoordinates(NearbyPlaceDto.GpsCoordinates gpsCoords) {
        if (gpsCoords == null) {
            return null;
        }

        return PlaceInfoResponse.GpsCoordinates.builder()
                .latitude(gpsCoords.getLatitude())
                .longitude(gpsCoords.getLongitude())
                .build();
    }

    /**
     * Construye PlaceInfoResponse completo con detalles del lugar
     * Crea respuesta completa incluyendo metadata de búsqueda
     * 
     * @param placeDetail Detalles del lugar ya mapeados
     * @return Respuesta completa con el lugar y metadata
     */
    public PlaceInfoResponse toPlaceInfoResponse(PlaceInfoResponse.PlaceDetail placeDetail) {
        if (placeDetail == null) {
            return PlaceInfoResponse.builder()
                    .localResults(Collections.emptyList())
                    .searchMetadata(buildSuccessMetadata())
                    .build();
        }

        return PlaceInfoResponse.builder()
                .localResults(List.of(placeDetail))
                .searchMetadata(buildSuccessMetadata())
                .build();
    }

    /**
     * Construye PlaceInfoResponse completo desde NearbyPlaceDto directamente
     * Método de conveniencia para mapeo directo en un solo paso
     * 
     * @param nearbyPlace DTO de SerpApi con información del lugar
     * @return Respuesta completa lista para devolver al cliente
     */
    public PlaceInfoResponse toPlaceInfoResponseFromNearby(NearbyPlaceDto nearbyPlace) {
        if (nearbyPlace == null) {
            return PlaceInfoResponse.builder()
                    .localResults(Collections.emptyList())
                    .searchMetadata(buildSuccessMetadata())
                    .build();
        }

        PlaceInfoResponse.PlaceDetail detail = toPlaceDetail(nearbyPlace);
        return toPlaceInfoResponse(detail);
    }

    /**
     * Convierte lista de NearbyPlaceDto a lista de PlaceDetail
     * Útil para mapear múltiples resultados de búsqueda
     * 
     * @param nearbyPlaces Lista de lugares desde SerpApi
     * @return Lista de detalles de lugares mapeados
     */
    public List<PlaceInfoResponse.PlaceDetail> toPlaceDetailList(List<NearbyPlaceDto> nearbyPlaces) {
        if (nearbyPlaces == null || nearbyPlaces.isEmpty()) {
            return Collections.emptyList();
        }

        return nearbyPlaces.stream()
                .map(this::toPlaceDetail)
                .collect(Collectors.toList());
    }

    /**
     * Extrae el primer venue de NearbySearchResponse
     * Obtiene el venue más relevante de los resultados de búsqueda
     * 
     * @param searchResponse Respuesta de búsqueda de SerpApi
     * @return Primer venue encontrado o null si no hay resultados
     */
    public NearbyPlaceDto extractFirstVenue(NearbySearchResponse searchResponse) {
        if (searchResponse == null) {
            return null;
        }

        // Caso 1: Resultado único directo
        if (searchResponse.getPlaceResults() != null) {
            return searchResponse.getPlaceResults();
        }

        // Caso 2: Lista de resultados
        if (searchResponse.getLocalResults() != null && !searchResponse.getLocalResults().isEmpty()) {
            return searchResponse.getLocalResults().get(0);
        }

        return null;
    }

    /**
     * Construye metadata de éxito para la respuesta
     * Crea información de metadata estándar para respuestas exitosas
     * 
     * @return Metadata indicando éxito en la operación
     */
    private PlaceInfoResponse.SearchMetadata buildSuccessMetadata() {
        return PlaceInfoResponse.SearchMetadata.builder()
                .status("Success")
                .build();
    }
}
