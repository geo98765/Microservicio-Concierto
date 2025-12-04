package com.example.concert_service.controller;

import java.math.BigDecimal;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.concert_service.dto.NearbySearchResponse;
import com.example.concert_service.dto.PlaceInfoResponse;
import com.example.concert_service.service.HotelService;

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
@RequestMapping("/api/v1/hotels")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Hotels", description = "Search for hotels near concert venues")
public class HotelController {

    private final HotelService hotelService;

    @Operation(summary = "Get hotels near venue", description = "List hotels near a specific venue ordered by distance.\n\n"
            +
            "**Functionality 28:** Includes name, location, rating, distance, and reviews.\n\n" +
            "**Maximum:** 20 results.\n" +
            "**Default radius:** 5,000 meters")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Hotels found successfully", content = @Content(schema = @Schema(implementation = NearbySearchResponse.class))),
            @ApiResponse(responseCode = "404", description = "Venue not found"),
            @ApiResponse(responseCode = "500", description = "Error searching hotels")
    })
    @GetMapping("/near-venue/{venueId}")
    public ResponseEntity<NearbySearchResponse> getHotelsNearVenue(
            @Parameter(description = "Venue ID in the database", example = "1", required = true) @PathVariable Integer venueId,
            @Parameter(description = "Maximum distance in meters", example = "5000") @RequestParam(required = false) Integer maxDistance) {

        log.info("Getting hotels near venue: {} with max distance: {}m", venueId, maxDistance);
        NearbySearchResponse response = hotelService.getHotelsNearVenue(venueId, maxDistance);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get complete hotel details", description = "Functionality 29: Returns detailed hotel information including user reviews.\n\n"
            +
            "**Includes:** name, address, rating, reviews, amenities, schedules, contact.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Hotel details retrieved successfully", content = @Content(schema = @Schema(implementation = PlaceInfoResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid parameters"),
            @ApiResponse(responseCode = "404", description = "Hotel not found")
    })
    @GetMapping("/details")
    public ResponseEntity<PlaceInfoResponse> getHotelDetails(
            @Parameter(description = "Hotel name", example = "Grand Hotel", required = true) @RequestParam String name,
            @Parameter(description = "Location coordinates (lat,lng)", example = "19.4326,-99.1332", required = true) @RequestParam String location) {

        log.info("Getting hotel details for: {} at location: {}", name, location);
        PlaceInfoResponse response = hotelService.getHotelDetails(name, location);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Filter hotels by rating", description = "Functionality 30: Get hotels with a minimum star rating (1-5 stars)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Filtered hotels retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid rating parameter"),
            @ApiResponse(responseCode = "404", description = "Venue not found")
    })
    @GetMapping("/filter-by-rating/{venueId}")
    public ResponseEntity<NearbySearchResponse> filterHotelsByRating(
            @Parameter(description = "Venue ID in the database", example = "1", required = true) @PathVariable Integer venueId,
            @Parameter(description = "Minimum rating (1-5)", example = "4.0", required = true) @RequestParam BigDecimal minRating) {

        log.info("Filtering hotels with min rating: {} for venue: {}", minRating, venueId);
        NearbySearchResponse response = hotelService.filterHotelsByRating(venueId, minRating);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Filter hotels by distance", description = "Functionality 31: Get hotels within a maximum distance from venue")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Filtered hotels retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid distance parameter"),
            @ApiResponse(responseCode = "404", description = "Venue not found")
    })
    @GetMapping("/filter-by-distance/{venueId}")
    public ResponseEntity<NearbySearchResponse> filterHotelsByDistance(
            @Parameter(description = "Venue ID in the database", example = "1", required = true) @PathVariable Integer venueId,
            @Parameter(description = "Maximum distance in meters", example = "2000", required = true) @RequestParam Integer maxDistance) {

        log.info("Filtering hotels with max distance: {}m for venue: {}", maxDistance, venueId);
        NearbySearchResponse response = hotelService.filterHotelsByDistance(venueId, maxDistance);
        return ResponseEntity.ok(response);
    }
}
