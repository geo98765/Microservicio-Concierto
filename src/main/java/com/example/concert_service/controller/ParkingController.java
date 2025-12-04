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
import com.example.concert_service.service.ParkingService;

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
@RequestMapping("/api/v1/parking")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Parking", description = "Endpoints for nearby parking lots")
public class ParkingController {

    private final ParkingService parkingService;

    @Operation(summary = "Get parking lots near venue", description = "Functionality 36: List parking lots near a specific venue.\n\n"
            +
            "**Includes:** capacity, price per hour, schedules, and distance.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Parking lots found successfully", content = @Content(schema = @Schema(implementation = NearbySearchResponse.class))),
            @ApiResponse(responseCode = "404", description = "Venue not found"),
            @ApiResponse(responseCode = "500", description = "Error searching parking lots")
    })
    @GetMapping("/near-venue/{venueId}")
    public ResponseEntity<NearbySearchResponse> getParkingsNearVenue(
            @Parameter(description = "Venue ID in the database", example = "1", required = true) @PathVariable Integer venueId) {

        log.info("Getting parking lots near venue: {}", venueId);
        NearbySearchResponse response = parkingService.getParkingsNearVenue(venueId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get parking lots within radius", description = "Search for parking lots within a specific radius (meters) from a venue")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Parking lots found successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid radius parameter"),
            @ApiResponse(responseCode = "404", description = "Venue not found")
    })
    @GetMapping("/within-radius/{venueId}")
    public ResponseEntity<NearbySearchResponse> getParkingsWithinRadius(
            @Parameter(description = "Venue ID in the database", example = "1", required = true) @PathVariable Integer venueId,
            @Parameter(description = "Search radius in meters", example = "1500", required = true) @RequestParam Integer radius) {

        log.info("Getting parking lots within {} meters of venue: {}", radius, venueId);
        NearbySearchResponse response = parkingService.getParkingsWithinRadius(venueId, radius);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get parking lot details", description = "Functionality 37: Retrieve complete information about a specific parking lot")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Parking details retrieved successfully", content = @Content(schema = @Schema(implementation = PlaceInfoResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid parameters"),
            @ApiResponse(responseCode = "404", description = "Parking lot not found")
    })
    @GetMapping("/details")
    public ResponseEntity<PlaceInfoResponse> getParkingDetails(
            @Parameter(description = "Parking lot name", example = "Central Parking", required = true) @RequestParam String name,
            @Parameter(description = "Location coordinates (lat,lng)", example = "19.4326,-99.1332", required = true) @RequestParam String location) {

        log.info("Getting parking details for: {} at location: {}", name, location);
        PlaceInfoResponse response = parkingService.getParkingDetails(name, location);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Filter parking lots by price", description = "Functionality 38: Get parking lots with a maximum price per hour")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Filtered parking lots retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid price parameter"),
            @ApiResponse(responseCode = "404", description = "Venue not found")
    })
    @GetMapping("/filter-by-price/{venueId}")
    public ResponseEntity<NearbySearchResponse> filterParkingsByPrice(
            @Parameter(description = "Venue ID in the database", example = "1", required = true) @PathVariable Integer venueId,
            @Parameter(description = "Maximum price per hour", example = "5.00", required = true) @RequestParam BigDecimal maxPrice) {

        log.info("Filtering parking lots with max price: {} for venue: {}", maxPrice, venueId);
        NearbySearchResponse response = parkingService.filterParkingsByPrice(venueId, maxPrice);
        return ResponseEntity.ok(response);
    }
}
