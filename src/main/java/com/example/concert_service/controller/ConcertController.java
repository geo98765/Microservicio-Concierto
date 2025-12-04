package com.example.concert_service.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.concert_service.dto.ConcertRequest;
import com.example.concert_service.dto.ConcertResponse;
import com.example.concert_service.service.ConcertService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/concerts")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Concerts", description = "Endpoints for concert management")
public class ConcertController {

    private final ConcertService concertService;

    @Operation(summary = "Create a new concert")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Concert created successfully", content = @Content(schema = @Schema(implementation = ConcertResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "Venue not found")
    })
    @PostMapping
    public ResponseEntity<ConcertResponse> createConcert(@Valid @RequestBody ConcertRequest request) {
        log.info("Creating concert: {}", request.getName());
        ConcertResponse response = concertService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Get concert by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Concert found", content = @Content(schema = @Schema(implementation = ConcertResponse.class))),
            @ApiResponse(responseCode = "404", description = "Concert not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ConcertResponse> getConcertById(@PathVariable Integer id) {
        log.info("Getting concert by ID: {}", id);
        return ResponseEntity.ok(concertService.findById(id));
    }

    @Operation(summary = "Get all concerts")
    @GetMapping
    public ResponseEntity<List<ConcertResponse>> getAllConcerts() {
        log.info("Getting all concerts");
        return ResponseEntity.ok(concertService.findAll());
    }

    @Operation(summary = "Search concerts by name")
    @GetMapping("/search")
    public ResponseEntity<List<ConcertResponse>> searchConcertsByName(@RequestParam String name) {
        log.info("Searching concerts by name: {}", name);
        return ResponseEntity.ok(concertService.searchByName(name));
    }

    @Operation(summary = "Get upcoming concerts")
    @GetMapping("/upcoming")
    public ResponseEntity<List<ConcertResponse>> getUpcomingConcerts() {
        log.info("Getting upcoming concerts");
        return ResponseEntity.ok(concertService.findUpcoming());
    }

    @Operation(summary = "Get concerts by venue")
    @GetMapping("/venue/{venueId}")
    public ResponseEntity<List<ConcertResponse>> getConcertsByVenue(@PathVariable Integer venueId) {
        log.info("Getting concerts for venue: {}", venueId);
        return ResponseEntity.ok(concertService.findByVenue(venueId));
    }

    @Operation(summary = "Update a concert")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Concert updated successfully"),
            @ApiResponse(responseCode = "404", description = "Concert or Venue not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ConcertResponse> updateConcert(
            @PathVariable Integer id,
            @Valid @RequestBody ConcertRequest request) {
        log.info("Updating concert ID: {}", id);
        return ResponseEntity.ok(concertService.update(id, request));
    }

    @Operation(summary = "Delete a concert")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Concert deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Concert not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteConcert(@PathVariable Integer id) {
        log.info("Deleting concert ID: {}", id);
        concertService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
