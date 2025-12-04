package com.example.concert_service.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.concert_service.dto.ConcertRequest;
import com.example.concert_service.dto.ConcertResponse;
import com.example.concert_service.mapper.ConcertMapper;
import com.example.concert_service.model.Concert;
import com.example.concert_service.model.Venue;
import com.example.concert_service.repository.ConcertRepository;
import com.example.concert_service.repository.VenueRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ConcertService {

    private final ConcertRepository concertRepository;
    private final VenueRepository venueRepository;
    private final ConcertMapper concertMapper;

    @Transactional
    public ConcertResponse create(ConcertRequest request) {
        log.info("Creando concierto: {}", request.getName());

        Venue venue = venueRepository.findById(request.getVenueId())
                .orElseThrow(() -> new RuntimeException("Venue no encontrado con ID: " + request.getVenueId()));

        Concert concert = concertMapper.toEntity(request, venue);
        concert = concertRepository.save(concert);

        return concertMapper.toResponse(concert);
    }

    public ConcertResponse findById(Integer concertId) {
        log.info("Buscando concierto con ID: {}", concertId);

        Concert concert = concertRepository.findById(concertId)
                .orElseThrow(() -> new RuntimeException("Concierto no encontrado con ID: " + concertId));

        return concertMapper.toResponse(concert);
    }

    public List<ConcertResponse> findAll() {
        log.info("Obteniendo todos los conciertos");

        return concertRepository.findAll().stream()
                .map(concertMapper::toResponse)
                .collect(Collectors.toList());
    }

    public List<ConcertResponse> findByStatus(String status) {
        log.info("Buscando conciertos con estado: {}", status);

        return concertRepository.findByStatus(status).stream()
                .map(concertMapper::toResponse)
                .collect(Collectors.toList());
    }

    public List<ConcertResponse> findUpcoming() {
        log.info("Buscando conciertos próximos");

        return concertRepository.findByDateTimeAfter(LocalDateTime.now()).stream()
                .map(concertMapper::toResponse)
                .collect(Collectors.toList());
    }

    public List<ConcertResponse> searchByName(String name) {
        log.info("Buscando conciertos por nombre: {}", name);

        return concertRepository.findByNameContainingIgnoreCase(name).stream()
                .map(concertMapper::toResponse)
                .collect(Collectors.toList());
    }

    public List<ConcertResponse> findByVenue(Integer venueId) {
        log.info("Buscando conciertos en venue: {}", venueId);

        return concertRepository.findByVenueVenueId(venueId).stream()
                .map(concertMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public ConcertResponse update(Integer concertId, ConcertRequest request) {
        log.info("Actualizando concierto con ID: {}", concertId);

        Concert concert = concertRepository.findById(concertId)
                .orElseThrow(() -> new RuntimeException("Concierto no encontrado con ID: " + concertId));

        Venue venue = venueRepository.findById(request.getVenueId())
                .orElseThrow(() -> new RuntimeException("Venue no encontrado con ID: " + request.getVenueId()));

        concert.setName(request.getName());
        concert.setDateTime(request.getDateTime());
        concert.setStatus(request.getStatus());
        concert.setPrice(request.getPrice());
        concert.setVenue(venue);

        concert = concertRepository.save(concert);

        return concertMapper.toResponse(concert);
    }

    @Transactional
    public void delete(Integer concertId) {
        log.info("Eliminando concierto con ID: {}", concertId);

        if (!concertRepository.existsById(concertId)) {
            throw new RuntimeException("Concierto no encontrado con ID: " + concertId);
        }

        concertRepository.deleteById(concertId);
    }
}
