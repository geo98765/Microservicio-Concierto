package com.example.concert_service.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.concert_service.dto.ArtistCompleteInfoResponse;
import com.example.concert_service.dto.ArtistResponse;
import com.example.concert_service.service.ArtistEventService;
import com.example.concert_service.service.SpotifyService;

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
@RequestMapping("/api/v1/artists")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Artists", description = "Endpoints for artist management and search")
public class ArtistController {

    private final SpotifyService spotifyService;
    private final ArtistEventService artistEventService;

    @Operation(summary = "Search artists by name", description = "Search for artists on Spotify by name. Returns up to 10 results with detailed information.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Artists found successfully", content = @Content(schema = @Schema(implementation = ArtistResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid search parameter"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/search")
    public ResponseEntity<Page<ArtistResponse>> searchArtists(
            @Parameter(description = "Artist name to search for", example = "Metallica", required = true) @RequestParam String name,
            @Parameter(description = "Pagination parameters (page, size)", example = "page=0&size=10") @PageableDefault(size = 10) Pageable pageable) {

        log.info("🔍 Searching artists: '{}' (page: {}, size: {})",
                name, pageable.getPageNumber(), pageable.getPageSize());

        // Obtener todos los artistas del servicio
        List<ArtistResponse> allArtists = spotifyService.searchArtistsByName(name);

        // Lógica de paginación manual
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), allArtists.size());

        List<ArtistResponse> pageContent = start < allArtists.size()
                ? allArtists.subList(start, end)
                : List.of();

        Page<ArtistResponse> page = new PageImpl<>(pageContent, pageable, allArtists.size());

        log.info("✅ Returning {} artists (total: {}, page: {}/{})",
                pageContent.size(),
                allArtists.size(),
                pageable.getPageNumber() + 1,
                page.getTotalPages());

        return ResponseEntity.ok(page);
    }

    @Operation(summary = "Get artist information by ID", description = "Returns detailed information about an artist using their Spotify ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Artist information retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Artist not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{spotifyId}")
    public ResponseEntity<ArtistResponse> getArtistById(
            @Parameter(description = "Spotify ID of the artist", example = "2ye2Wgw4gimLv2eAKyk1NB", required = true) @PathVariable String spotifyId) {

        log.info("Getting artist information: {}", spotifyId);
        ArtistResponse artist = spotifyService.getArtistById(spotifyId);
        return ResponseEntity.ok(artist);
    }

    @Operation(summary = "Get complete artist information with events", description = "Returns comprehensive artist information including:\n"
            +
            "- Artist data from Spotify\n" +
            "- Upcoming events from Ticketmaster\n" +
            "- Venue information\n" +
            "- Weather data\n" +
            "- Nearby hotels (top 5)\n" +
            "- Public transport options")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Complete information retrieved successfully", content = @Content(schema = @Schema(implementation = ArtistCompleteInfoResponse.class))),
            @ApiResponse(responseCode = "404", description = "Artist not found"),
            @ApiResponse(responseCode = "500", description = "Error processing request")
    })
    @GetMapping("/{spotifyId}/complete-info")
    public ResponseEntity<ArtistCompleteInfoResponse> getArtistCompleteInfo(
            @Parameter(description = "Spotify ID of the artist", example = "4q3ewBCX7sLwd24euuV69X", required = true) @PathVariable String spotifyId) {

        log.info("🎸 Getting complete artist information: {}", spotifyId);
        ArtistCompleteInfoResponse response = artistEventService.getArtistCompleteInfo(spotifyId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get multiple artists by IDs", description = "Returns information for multiple artists by providing a list of Spotify IDs")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Artists retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid IDs list")
    })
    @GetMapping("/multiple")
    public ResponseEntity<List<ArtistResponse>> getArtistsByIds(
            @Parameter(description = "Comma-separated list of Spotify IDs", example = "2ye2Wgw4gimLv2eAKyk1NB,4q3ewBCX7sLwd24euuV69X", required = true) @RequestParam List<String> ids) {

        log.info("Getting {} artists", ids.size());
        List<ArtistResponse> artists = spotifyService.getArtistsByIds(ids);
        return ResponseEntity.ok(artists);
    }
}
