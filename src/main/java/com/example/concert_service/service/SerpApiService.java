package com.example.concert_service.service;

import java.io.IOException;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.concert_service.config.SerpApiConfig;
import com.example.concert_service.dto.NearbySearchResponse;
import com.example.concert_service.dto.PlaceInfoResponse;
import com.example.concert_service.dto.WeatherResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@Service
@RequiredArgsConstructor
@Slf4j
public class SerpApiService {

    private final SerpApiConfig config;
    private final OkHttpClient httpClient = new OkHttpClient();

    private final Gson gson = new GsonBuilder()
            .setLenient()
            .registerTypeAdapter(new TypeToken<List<String>>() {
            }.getType(),
                    (JsonDeserializer<List<String>>) (json, typeOfT, context) -> {
                        List<String> list = new ArrayList<>();
                        if (json.isJsonArray()) {
                            json.getAsJsonArray().forEach(e -> list.add(e.getAsString()));
                        } else if (json.isJsonPrimitive()) {
                            list.add(json.getAsString());
                        }
                        return list;
                    })
            .create();

    private <T> T executeRequest(String url, Type responseType) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            String responseBody = response.body() != null ? response.body().string() : "";

            if (!response.isSuccessful()) {
                log.error("❌ Error en la petición: {}. Response: {}", response.code(), responseBody);
                throw new IOException("Error en la petición: " + response.code());
            }

            try {
                T result = gson.fromJson(responseBody, responseType);

                if (result == null) {
                    log.error("❌ La respuesta de SerpAPI fue null después de parsear");
                    throw new IOException("Respuesta null de SerpAPI");
                }

                return result;
            } catch (JsonSyntaxException e) {
                log.error("❌ Error parseando JSON de SerpAPI: {}", e.getMessage());
                throw new IOException("Error parseando respuesta de SerpAPI: " + e.getMessage(), e);
            }
        }
    }

    private <T> T executeRequest(String url, Class<T> responseClass) throws IOException {
        return executeRequest(url, (Type) responseClass);
    }

    public NearbySearchResponse searchVenuesByQuery(String query) {
        try {
            String url = String.format(
                    "%s?engine=google_maps&type=search&q=%s&api_key=%s",
                    config.getBaseUrl(),
                    query.replace(" ", "+"),
                    config.getApiKey());

            log.info("🔍 Buscando venues con query: {}", query);

            return executeRequest(url, NearbySearchResponse.class);
        } catch (Exception e) {
            log.error("❌ Error buscando venues: {}", e.getMessage(), e);
            throw new RuntimeException("Error al buscar venues: " + e.getMessage(), e);
        }
    }

    public PlaceInfoResponse getPlaceDetailsByPlaceId(String placeId) {
        try {
            String url = String.format(
                    "%s?engine=google_maps&type=place&data_id=%s&api_key=%s",
                    config.getBaseUrl(),
                    placeId,
                    config.getApiKey());

            log.info("🔍 Obteniendo detalles del lugar con Place ID: {}", placeId);

            PlaceInfoResponse response = executeRequest(url, PlaceInfoResponse.class);

            if (response == null || response.getLocalResults() == null || response.getLocalResults().isEmpty()) {
                log.warn("⚠️  No se obtuvieron detalles del lugar. Respuesta vacía.");
            } else {
                log.info("✅ Detalles obtenidos: {}", response.getLocalResults().get(0).getTitle());
            }

            return response;

        } catch (Exception e) {
            log.error("❌ Error obteniendo detalles del lugar: {}", e.getMessage(), e);
            throw new RuntimeException("Error al obtener detalles del lugar", e);
        }
    }

    public NearbySearchResponse searchNearbyPlaces(
            BigDecimal latitude,
            BigDecimal longitude,
            String placeType,
            Integer radius) {
        try {
            String query = String.format("%s near %s,%s", placeType, latitude.toString(), longitude.toString());
            String url = String.format(
                    "%s?engine=google_maps&type=search&q=%s&api_key=%s",
                    config.getBaseUrl(),
                    query.replace(" ", "+"),
                    config.getApiKey());

            log.info("🔍 Buscando lugares: {} en {},{}", placeType, latitude, longitude);
            return executeRequest(url, NearbySearchResponse.class);
        } catch (Exception e) {
            log.error("❌ Error buscando lugares cercanos: {}", e.getMessage());
            throw new RuntimeException("Error al buscar lugares cercanos", e);
        }
    }

    public NearbySearchResponse searchNearbyHotels(BigDecimal latitude, BigDecimal longitude, Integer radius) {
        try {
            String query = String.format("hotels near %s,%s", latitude.toString(), longitude.toString());
            String url = String.format(
                    "%s?engine=google_maps&type=search&q=%s&api_key=%s",
                    config.getBaseUrl(),
                    query.replace(" ", "+"),
                    config.getApiKey());

            log.info("🏨 Buscando hoteles en: {},{}", latitude, longitude);
            return executeRequest(url, NearbySearchResponse.class);
        } catch (Exception e) {
            log.error("❌ Error buscando hoteles: {}", e.getMessage());
            throw new RuntimeException("Error al buscar hoteles", e);
        }
    }

    public NearbySearchResponse searchNearbyRestaurants(BigDecimal latitude, BigDecimal longitude, Integer radius) {
        try {
            String query = String.format("restaurants near %s,%s", latitude.toString(), longitude.toString());
            String url = String.format(
                    "%s?engine=google_maps&type=search&q=%s&api_key=%s",
                    config.getBaseUrl(),
                    query.replace(" ", "+"),
                    config.getApiKey());

            log.info("🍽️  Buscando restaurantes en: {},{}", latitude, longitude);
            return executeRequest(url, NearbySearchResponse.class);
        } catch (Exception e) {
            log.error("❌ Error buscando restaurantes: {}", e.getMessage());
            throw new RuntimeException("Error al buscar restaurantes", e);
        }
    }

    public NearbySearchResponse searchNearbyParkings(BigDecimal latitude, BigDecimal longitude, Integer radius) {
        try {
            String query = String.format("parking near %s,%s", latitude.toString(), longitude.toString());
            String url = String.format(
                    "%s?engine=google_maps&type=search&q=%s&api_key=%s",
                    config.getBaseUrl(),
                    query.replace(" ", "+"),
                    config.getApiKey());

            log.info("🅿️  Buscando estacionamientos en: {},{}", latitude, longitude);
            return executeRequest(url, NearbySearchResponse.class);
        } catch (Exception e) {
            log.error("❌ Error buscando estacionamientos: {}", e.getMessage());
            throw new RuntimeException("Error al buscar estacionamientos", e);
        }
    }

    public NearbySearchResponse searchNearbyTransport(BigDecimal latitude, BigDecimal longitude) {
        try {
            String query = String.format("public transport near %s,%s", latitude.toString(), longitude.toString());
            String url = String.format(
                    "%s?engine=google_maps&type=search&q=%s&api_key=%s",
                    config.getBaseUrl(),
                    query.replace(" ", "+"),
                    config.getApiKey());

            log.info("🚇 Buscando transporte público en: {},{}", latitude, longitude);
            return executeRequest(url, NearbySearchResponse.class);
        } catch (Exception e) {
            log.error("❌ Error buscando transporte: {}", e.getMessage());
            throw new RuntimeException("Error al buscar transporte", e);
        }
    }

    public PlaceInfoResponse getPlaceDetails(String placeName, String location) {
        try {
            String url = String.format(
                    "%s?engine=google_maps&type=search&q=%s&ll=%s&api_key=%s",
                    config.getBaseUrl(),
                    placeName.replace(" ", "+"),
                    location,
                    config.getApiKey());

            log.info("🔍 Obteniendo detalles de: {}", placeName);
            return executeRequest(url, PlaceInfoResponse.class);
        } catch (Exception e) {
            log.error("❌ Error obteniendo detalles del lugar: {}", e.getMessage());
            throw new RuntimeException("Error al obtener detalles del lugar", e);
        }
    }

    public WeatherResponse getWeatherByLocation(String location) {
        try {
            String url = String.format(
                    "%s?engine=google&q=weather+%s&api_key=%s",
                    config.getBaseUrl(),
                    location.replace(" ", "+"),
                    config.getApiKey());

            log.info("🌤️  Obteniendo clima para: {}", location);

            WeatherResponse response = executeRequest(url, WeatherResponse.class);

            if (response != null && response.getAnswerBox() != null) {
                log.info("✅ Clima obtenido: {} - {}",
                        response.getAnswerBox().getLocation(),
                        response.getAnswerBox().getWeather());
            } else {
                log.warn("⚠️  Respuesta de clima vacía para: {}", location);
            }

            return response;

        } catch (Exception e) {
            log.error("❌ Error obteniendo clima para {}: {}", location, e.getMessage());
            return null;
        }
    }

    public PlaceInfoResponse searchVenueInfo(String venueName, String location) {
        try {
            String query = venueName + " " + location;
            String url = String.format(
                    "%s?engine=google_maps&type=search&q=%s&api_key=%s",
                    config.getBaseUrl(),
                    query.replace(" ", "+"),
                    config.getApiKey());

            log.info("🎪 Buscando información del venue: {}", venueName);
            return executeRequest(url, PlaceInfoResponse.class);
        } catch (Exception e) {
            log.error("❌ Error buscando información del venue: {}", e.getMessage());
            throw new RuntimeException("Error al buscar información del venue", e);
        }
    }
}
