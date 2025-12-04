package com.example.concert_service.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConcertResponse {
    private Integer concertId;
    private String name;
    private LocalDateTime dateTime;
    private String status;
    private BigDecimal price;
    private String venueName;
    private String venueCity;
}
