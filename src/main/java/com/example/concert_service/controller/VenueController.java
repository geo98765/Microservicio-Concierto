package com.example.concert_service.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.concert_service.dto.NearbyPlaceDto;
import com.example.concert_service.dto.PlaceInfoResponse;
import com.example.concert_service.service.VenueService;

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
@RequestMapping("/api/v1/venues")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Venues", description = "Concert venue search and nearby services management")
public class VenueController {

    private final VenueService venueService;

    // ===== VENUE SEARCH =====

    @Operation(summary = "Search venues in Google Maps", description = "Search concert venues directly in Google Maps using text query with pagination support.\n\n"
            +
            "**Search examples:**\n" +
            "- 'Foro Sol Ciudad de México'\n" +
            "- 'Madison Square Garden'\n" +
            "- 'Estadio Azteca'")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Venues found successfully", content = @Content(schema = @Schema(implementation = Page.class))),
            @ApiResponse(responseCode = "400", description = "Invalid query"),
            @ApiResponse(responseCode = "500", description = "Error querying Google Maps")
    })
    @GetMapping("/search-maps")
    public ResponseEntity<Page<NearbyPlaceDto>> searchVenuesInGoogleMaps(
            @Parameter(description = "Venue name or search query", example = "Foro Sol Ciudad Mexico", required = true) @RequestParam String query,

            @Parameter(description = "Page number (starts at 0)", example = "0") @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "Number of venues per page", example = "10") @RequestParam(defaultValue = "10") int pageSize) {

        log.info("Searching venues in Google Maps: {} (page: {}, size: {})", query, page, pageSize);

        Pageable pageable = PageRequest.of(page, pageSize);
        Page<NearbyPlaceDto> response = venueService.searchVenuesInGoogleMaps(query, pageable);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Search venues by location", description = "Search venues near specific coordinates with pagination support.\n\n"
            +
            "Useful for finding venues near user's location.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Venues found successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid coordinates")
    })
    @GetMapping("/search-maps-by-location")
    public ResponseEntity<Page<NearbyPlaceDto>> searchVenuesByLocation(
            @Parameter(description = "Latitude", example = "19.4326", required = true) @RequestParam Double lat,

            @Parameter(description = "Longitude", example = "-99.1332", required = true) @RequestParam Double lng,

            @Parameter(description = "Type of place to search", example = "concert venue") @RequestParam(defaultValue = "concert venue") String query,

            @Parameter(description = "Page number (starts at 0)", example = "0") @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "Number of venues per page", example = "10") @RequestParam(defaultValue = "10") int pageSize) {

        log.info("Searching venues by location: {},{} with query: {} (page: {}, size: {})",
                lat, lng, query, page, pageSize);

        Pageable pageable = PageRequest.of(page, pageSize);
        Page<NearbyPlaceDto> response = venueService.searchVenuesByLocation(lat, lng, query, pageable);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get venue details", description = "Returns complete venue information by searching by name.\n\n"
            +
            "**UPDATED:** Now searches by name instead of Place ID for better results.\n\n" +
            "**Includes:** address, coordinates, reviews, rating, phone, website, place type.\n\n" +
            "**Example usage:**\n" +
            "1. First search venues with `/search-maps?query=Foro Sol`\n" +
            "2. Copy the exact name from results\n" +
            "3. Use that name in this endpoint to get complete details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Venue details retrieved successfully", content = @Content(schema = @Schema(implementation = PlaceInfoResponse.class))),
            @ApiResponse(responseCode = "404", description = "Venue not found")
    })
    @GetMapping("/details")
    public ResponseEntity<PlaceInfoResponse> getVenueDetails(
            @Parameter(description = "Complete venue name or search query", example = "Lefrak Concert Hall New York", required = true) @RequestParam String query) {

        log.info("Getting venue details for: {}", query);

        PlaceInfoResponse response = venueService.getVenueDetails(query);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Find nearby venues", description = "Find venues within a specific radius from given coordinates with pagination support.\n\n"
            +
            "**Default radius:** 10,000 meters (10 km)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Venues found successfully")
    })
    @GetMapping("/nearby")
    public ResponseEntity<Page<NearbyPlaceDto>> findVenuesNearby(
            @Parameter(description = "Latitude", example = "19.4326", required = true) @RequestParam Double lat,

            @Parameter(description = "Longitude", example = "-99.1332", required = true) @RequestParam Double lng,

            @Parameter(description = "Search radius in meters", example = "10000") @RequestParam(defaultValue = "10000") Integer radius,

            @Parameter(description = "Page number (starts at 0)", example = "0") @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "Number of venues per page", example = "10") @RequestParam(defaultValue = "10") int pageSize) {

        log.info("Finding venues nearby: {},{} with radius: {}m (page: {}, size: {})",
                lat, lng, radius, page, pageSize);

        Pageable pageable = PageRequest.of(page, pageSize);
        Page<NearbyPlaceDto> response = venueService.findVenuesNearby(lat, lng, radius, pageable);

        return ResponseEntity.ok(response);
    }

    // ===== NEARBY SERVICES =====

    @Operation(summary = "Get hotels near venue", description = "List hotels near a specific venue, ordered by distance with pagination support.\n\n"
            +
            "**Note:** Use venue name or Place ID as parameter.\n\n" +
            "**Default radius:** 5,000 meters (5 km)\n" +
            "**Functionality #28:** Maximum 20 results per page recommended")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Hotels found successfully", content = @Content(schema = @Schema(implementation = Page.class)))
    })
    @GetMapping("/hotels")
    public ResponseEntity<Page<NearbyPlaceDto>> getHotelsNearVenue(
            @Parameter(description = "Venue name or Place ID", example = "Foro Sol", required = true) @RequestParam String placeId,

            @Parameter(description = "Search radius in meters", example = "5000") @RequestParam(defaultValue = "5000") Integer radius,

            @Parameter(description = "Page number (starts at 0)", example = "0") @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "Number of hotels per page", example = "20") @RequestParam(defaultValue = "20") int pageSize) {

        log.info("Getting hotels near venue: {} with radius: {}m (page: {}, size: {})",
                placeId, radius, page, pageSize);

        Pageable pageable = PageRequest.of(page, pageSize);
        Page<NearbyPlaceDto> response = venueService.getHotelsNearVenue(placeId, radius, pageable);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get restaurants near venue", description = "List restaurants near the venue with pagination support.\n\n"
            +
            "**Default radius:** 2,000 meters (2 km)")
    @GetMapping("/restaurants")
    public ResponseEntity<Page<NearbyPlaceDto>> getRestaurantsNearVenue(
            @Parameter(description = "Venue name or Place ID", required = true) @RequestParam String placeId,

            @Parameter(description = "Search radius in meters", example = "2000") @RequestParam(defaultValue = "2000") Integer radius,

            @Parameter(description = "Page number (starts at 0)", example = "0") @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "Number of restaurants per page", example = "15") @RequestParam(defaultValue = "15") int pageSize) {

        log.info("Getting restaurants near venue: {} with radius: {}m (page: {}, size: {})",
                placeId, radius, page, pageSize);

        Pageable pageable = PageRequest.of(page, pageSize);
        Page<NearbyPlaceDto> response = venueService.getRestaurantsNearVenue(placeId, radius, pageable);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get parking lots near venue", description = "List parking lots near the venue with capacity and pricing information with pagination support.\n\n"
            +
            "**Functionality #36:** Includes capacity, price per hour, schedules, and distance.")
    @GetMapping("/parking")
    public ResponseEntity<Page<NearbyPlaceDto>> getParkingNearVenue(
            @Parameter(description = "Venue name or Place ID", required = true) @RequestParam String placeId,

            @Parameter(description = "Page number (starts at 0)", example = "0") @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "Number of parking lots per page", example = "10") @RequestParam(defaultValue = "10") int pageSize) {

        log.info("Getting parking near venue: {} (page: {}, size: {})", placeId, page, pageSize);

        Pageable pageable = PageRequest.of(page, pageSize);
        Page<NearbyPlaceDto> response = venueService.getParkingNearVenue(placeId, pageable);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get public transport near venue", description = "List public transport options (metro, bus, train) near the venue with pagination support.\n\n"
            +
            "**Functionality #32:** Includes nearby stations/stops.")
    @GetMapping("/transport")
    public ResponseEntity<Page<NearbyPlaceDto>> getTransportNearVenue(
            @Parameter(description = "Venue name or Place ID", required = true) @RequestParam String placeId,

            @Parameter(description = "Page number (starts at 0)", example = "0") @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "Number of transport options per page", example = "15") @RequestParam(defaultValue = "15") int pageSize) {

        log.info("Getting transport near venue: {} (page: {}, size: {})", placeId, page, pageSize);

        Pageable pageable = PageRequest.of(page, pageSize);
        Page<NearbyPlaceDto> response = venueService.getTransportNearVenue(placeId, pageable);

        return ResponseEntity.ok(response);
    }
}
