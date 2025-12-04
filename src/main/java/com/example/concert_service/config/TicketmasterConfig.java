package com.example.concert_service.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;

@Configuration
@Getter
public class TicketmasterConfig {

    @Value("${ticketmaster.api.key}")
    private String apiKey;

    @Value("${ticketmaster.base.url:https://app.ticketmaster.com/discovery/v2}")
    private String baseUrl;

    public String getEventsUrl() {
        return baseUrl + "/events.json";
    }

    public String getVenuesUrl() {
        return baseUrl + "/venues.json";
    }

    public String getAttractionsUrl() {
        return baseUrl + "/attractions.json";
    }
}
