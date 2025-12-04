package com.example.concert_service.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;

@Configuration
@Getter
public class SerpApiConfig {

    @Value("${serpapi.api.key}")
    private String apiKey;

    @Value("${serpapi.base.url:https://serpapi.com/search}")
    private String baseUrl;

    public String getGoogleMapsUrl() {
        return baseUrl;
    }

    public String getGoogleLocalUrl() {
        return baseUrl;
    }
}
