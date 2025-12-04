package com.example.concert_service.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de respuesta para información de artista desde Spotify
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArtistResponse {

    private String spotifyId;
    private String name;
    private List<String> genres;
    private Integer popularity; // 0-100
    private Integer followers;
    private String imageUrl;
    private String externalUrl; // URL de Spotify del artista
}
