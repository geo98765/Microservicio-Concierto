package com.example.concert_service.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.concert_service.dto.NearbySearchResponse;
import com.example.concert_service.dto.PlaceInfoResponse;
import com.example.concert_service.service.TransportService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/transport")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Public Transport", description = "Public transport options near venues")
public class TransportController {

    private final TransportService transportService;

    @Operation(summary = "Get available transport options", description = "Functionality 32: List all public transport options near the venue.\n\n"
            +
            "**Includes:**\n" +
            "- Metro stations\n" +
            "- Bus stops\n" +
            "- Train stations\n" +
            "- Distance and location information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transport options found", content = @Content(schema = @Schema(implementation = NearbySearchResponse.class))),
            @ApiResponse(responseCode = "404", description = "Venue not found")
    })
    @GetMapping("/options/{venueId}")
    public ResponseEntity<NearbySearchResponse> getTransportOptions(
            @Parameter(description = "Venue ID", example = "1", required = true) @PathVariable Integer venueId) {

        log.info("Getting transport options for venue: {}", venueId);
        NearbySearchResponse response = transportService.getTransportOptions(venueId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get nearby metro stations", description = "Search for metro stations within 1 km of the venue.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Metro stations found"),
            @ApiResponse(responseCode = "404", description = "Venue not found")
    })
    @GetMapping("/metro/{venueId}")
    public ResponseEntity<NearbySearchResponse> getNearbyMetroStations(
            @Parameter(description = "Venue ID", example = "1", required = true) @PathVariable Integer venueId) {

        log.info("Getting metro stations near venue: {}", venueId);
        NearbySearchResponse response = transportService.getNearbyMetroStations(venueId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get nearby bus stops", description = "Search for bus stops within 800 meters of the venue.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Bus stops found"),
            @ApiResponse(responseCode = "404", description = "Venue not found")
    })
    @GetMapping("/bus/{venueId}")
    public ResponseEntity<NearbySearchResponse> getNearbyBusStops(
            @Parameter(description = "Venue ID", example = "1", required = true) @PathVariable Integer venueId) {

        log.info("Getting bus stops near venue: {}", venueId);
        NearbySearchResponse response = transportService.getNearbyBusStops(venueId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get nearby train stations", description = "Search for train stations within 2 km of the venue.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Train stations found"),
            @ApiResponse(responseCode = "404", description = "Venue not found")
    })
    @GetMapping("/train/{venueId}")
    public ResponseEntity<NearbySearchResponse> getNearbyTrainStations(
            @Parameter(description = "Venue ID", example = "1", required = true) @PathVariable Integer venueId) {

        log.info("Getting train stations near venue: {}", venueId);
        NearbySearchResponse response = transportService.getNearbyTrainStations(venueId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get specific transport details", description = "Functionality 33: Returns detailed information about a specific station or stop.\n\n"
            +
            "**Includes:** schedules, lines, user reviews, accessibility.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transport details retrieved successfully", content = @Content(schema = @Schema(implementation = PlaceInfoResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid parameters"),
            @ApiResponse(responseCode = "404", description = "Transport station not found")
    })
    @GetMapping("/details")
    public ResponseEntity<PlaceInfoResponse> getTransportDetails(
            @Parameter(description = "Station or stop name", example = "Central Metro Station", required = true) @RequestParam String name,
            @Parameter(description = "Location coordinates (lat,lng)", example = "19.4326,-99.1332", required = true) @RequestParam String location) {

        log.info("Getting transport details for: {} at location: {}", name, location);
        PlaceInfoResponse response = transportService.getTransportDetails(name, location);
        return ResponseEntity.ok(response);
    }
}
