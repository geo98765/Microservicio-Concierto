package com.example.concert_service.service;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;

import com.example.concert_service.config.TicketmasterConfig;
import com.example.concert_service.dto.TicketmasterEventResponse;
import com.google.gson.Gson;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@Service
@RequiredArgsConstructor
@Slf4j
public class TicketmasterService {

    private final TicketmasterConfig config;
    private final Gson gson = new Gson();
    private final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build();

    public TicketmasterEventResponse searchEventsByArtist(String artistName) {
        try {
            String url = String.format(
                    "%s?keyword=%s&classificationName=music&apikey=%s&size=20&sort=date,asc",
                    config.getEventsUrl(),
                    artistName.replace(" ", "%20"),
                    config.getApiKey());

            log.info("🎫 Buscando eventos en Ticketmaster para: {}", artistName);

            return executeRequest(url);

        } catch (Exception e) {
            log.error("❌ Error buscando eventos en Ticketmaster: {}", e.getMessage(), e);
            throw new RuntimeException("Error al buscar eventos: " + e.getMessage(), e);
        }
    }

    public TicketmasterEventResponse searchEventsByArtistAndCountry(String artistName, String countryCode) {
        try {
            String url = String.format(
                    "%s?keyword=%s&classificationName=music&countryCode=%s&apikey=%s&size=20&sort=date,asc",
                    config.getEventsUrl(),
                    artistName.replace(" ", "%20"),
                    countryCode,
                    config.getApiKey());

            log.info("🎫 Buscando eventos en Ticketmaster para: {} en {}", artistName, countryCode);

            return executeRequest(url);

        } catch (Exception e) {
            log.error("❌ Error buscando eventos: {}", e.getMessage());
            throw new RuntimeException("Error al buscar eventos: " + e.getMessage(), e);
        }
    }

    public TicketmasterEventResponse searchEventsByVenue(String venueId) {
        try {
            String url = String.format(
                    "%s?venueId=%s&apikey=%s&size=20&sort=date,asc",
                    config.getEventsUrl(),
                    venueId,
                    config.getApiKey());

            log.info("🎫 Buscando eventos en venue: {}", venueId);

            return executeRequest(url);

        } catch (Exception e) {
            log.error("❌ Error buscando eventos por venue: {}", e.getMessage());
            throw new RuntimeException("Error al buscar eventos: " + e.getMessage(), e);
        }
    }

    public TicketmasterEventResponse.Event getEventById(String eventId) {
        try {
            String url = String.format(
                    "%s/%s.json?apikey=%s",
                    config.getBaseUrl() + "/events",
                    eventId,
                    config.getApiKey());

            log.info("🎫 Obteniendo detalles del evento: {}", eventId);

            Request request = new Request.Builder()
                    .url(url)
                    .get()
                    .build();

            try (Response response = client.newCall(request).execute()) {
                String responseBody = response.body() != null ? response.body().string() : "";

                if (!response.isSuccessful()) {
                    log.error("Error en la petición: {}. Response: {}", response.code(), responseBody);
                    throw new IOException("Error en la petición: " + response.code());
                }

                return gson.fromJson(responseBody, TicketmasterEventResponse.Event.class);
            }

        } catch (Exception e) {
            log.error("❌ Error obteniendo evento: {}", e.getMessage());
            throw new RuntimeException("Error al obtener evento: " + e.getMessage(), e);
        }
    }

    private TicketmasterEventResponse executeRequest(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("Accept", "application/json")
                .build();

        try (Response response = client.newCall(request).execute()) {
            String responseBody = response.body() != null ? response.body().string() : "";

            if (!response.isSuccessful()) {
                log.error("Error en la petición: {}. Response: {}", response.code(), responseBody);
                throw new IOException("Error en la petición: " + response.code());
            }

            TicketmasterEventResponse eventResponse = gson.fromJson(responseBody, TicketmasterEventResponse.class);

            if (eventResponse.getEmbedded() != null && eventResponse.getEmbedded().getEvents() != null) {
                log.info("✅ Encontrados {} eventos", eventResponse.getEmbedded().getEvents().size());
            } else {
                log.warn("⚠️  No se encontraron eventos");
            }

            return eventResponse;
        }
    }
}
