package com.example.concert_service.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.hc.core5.http.ParseException;
import org.springframework.stereotype.Service;

import com.example.concert_service.config.SpotifyApiConfig;
import com.example.concert_service.dto.ArtistResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.specification.Artist;
import se.michaelthelin.spotify.model_objects.specification.Paging;
import se.michaelthelin.spotify.requests.data.artists.GetArtistRequest;
import se.michaelthelin.spotify.requests.data.search.simplified.SearchArtistsRequest;

@Service
@RequiredArgsConstructor
@Slf4j
public class SpotifyService {

    private final SpotifyApi spotifyApi;
    private final SpotifyApiConfig spotifyApiConfig;

    /**
     * Buscar artistas por nombre
     */
    public List<ArtistResponse> searchArtistsByName(String name) {
        log.info("Buscando artistas en Spotify: {}", name);

        try {
            // Obtener access token
            spotifyApiConfig.getAccessToken(spotifyApi);

            // Crear request de búsqueda
            SearchArtistsRequest searchArtistsRequest = spotifyApi.searchArtists(name)
                    .limit(10) // Limitar a 10 resultados
                    .build();

            // Ejecutar búsqueda
            Paging<Artist> artistPaging = searchArtistsRequest.execute();

            // Convertir resultados a DTOs
            return Arrays.stream(artistPaging.getItems())
                    .map(this::mapToArtistResponse)
                    .collect(Collectors.toList());

        } catch (IOException | SpotifyWebApiException | ParseException e) {
            log.error("Error buscando artistas: {}", e.getMessage(), e);
            throw new RuntimeException("Error al buscar artistas en Spotify: " + e.getMessage(), e);
        }
    }

    /**
     * Obtener información de un artista por Spotify ID
     */
    public ArtistResponse getArtistById(String spotifyId) {
        log.info("Obteniendo información del artista: {}", spotifyId);

        try {
            // Obtener access token
            spotifyApiConfig.getAccessToken(spotifyApi);

            // Crear request para obtener artista
            GetArtistRequest getArtistRequest = spotifyApi.getArtist(spotifyId).build();

            // Ejecutar request
            Artist artist = getArtistRequest.execute();

            // Convertir a DTO
            return mapToArtistResponse(artist);

        } catch (IOException | SpotifyWebApiException | ParseException e) {
            log.error("Error obteniendo artista: {}", e.getMessage(), e);
            throw new RuntimeException("Error al obtener información del artista: " + e.getMessage(), e);
        }
    }

    /**
     * Obtener múltiples artistas por IDs
     */
    public List<ArtistResponse> getArtistsByIds(List<String> spotifyIds) {
        log.info("Obteniendo {} artistas", spotifyIds.size());

        List<ArtistResponse> artists = new ArrayList<>();

        for (String spotifyId : spotifyIds) {
            try {
                ArtistResponse artist = getArtistById(spotifyId);
                artists.add(artist);
            } catch (Exception e) {
                log.warn("Error obteniendo artista {}: {}", spotifyId, e.getMessage());
            }
        }

        return artists;
    }

    /**
     * Mapear Artist de Spotify a ArtistResponse DTO
     */
    private ArtistResponse mapToArtistResponse(Artist artist) {
        return ArtistResponse.builder()
                .spotifyId(artist.getId())
                .name(artist.getName())
                .genres(artist.getGenres() != null ? Arrays.asList(artist.getGenres()) : new ArrayList<>())
                .popularity(artist.getPopularity())
                .followers(artist.getFollowers() != null ? artist.getFollowers().getTotal() : 0)
                .imageUrl(artist.getImages() != null && artist.getImages().length > 0
                        ? artist.getImages()[0].getUrl()
                        : null)
                .externalUrl(artist.getExternalUrls() != null
                        ? artist.getExternalUrls().get("spotify")
                        : null)
                .build();
    }
}
