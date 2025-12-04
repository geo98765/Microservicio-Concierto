package com.example.concert_service.config;

import java.io.IOException;

import org.apache.hc.core5.http.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.credentials.ClientCredentials;
import se.michaelthelin.spotify.requests.authorization.client_credentials.ClientCredentialsRequest;

@Configuration
public class SpotifyApiConfig {

    @Value("${spotify.client.id}")
    private String clientId;

    @Value("${spotify.client.secret}")
    private String clientSecret;

    @Bean
    public SpotifyApi spotifyApi() {
        return new SpotifyApi.Builder()
                .setClientId(clientId)
                .setClientSecret(clientSecret)
                .build();
    }

    /**
     * Obtiene el access token de Spotify usando Client Credentials Flow
     */
    public String getAccessToken(SpotifyApi spotifyApi) {
        try {
            ClientCredentialsRequest clientCredentialsRequest = spotifyApi.clientCredentials().build();
            ClientCredentials clientCredentials = clientCredentialsRequest.execute();

            // Configurar el access token en el SpotifyApi
            spotifyApi.setAccessToken(clientCredentials.getAccessToken());

            return clientCredentials.getAccessToken();
        } catch (IOException | SpotifyWebApiException | ParseException e) {
            throw new RuntimeException("Error al obtener el access token de Spotify: " + e.getMessage(), e);
        }
    }
}
