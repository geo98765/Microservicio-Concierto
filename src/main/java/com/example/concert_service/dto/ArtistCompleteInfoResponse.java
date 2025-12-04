package com.example.concert_service.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO que contiene toda la información del artista + eventos + servicios
 * cercanos
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArtistCompleteInfoResponse {

    // Información del artista desde Spotify
    private ArtistResponse artist;

    // Eventos/conciertos próximos del artista
    private List<ArtistEventInfo> upcomingEvents;

    // Metadata
    private Integer totalEventsFound;
    private String searchedQuery;
    private String message;
}
