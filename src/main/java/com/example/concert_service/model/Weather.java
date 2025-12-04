package com.example.concert_service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "weather")
public class Weather {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "weather_id", nullable = false)
    private Integer weatherId;

    @Column(name = "weather_type", length = 50)
    private String weatherType;

    @Column(name = "temperature", precision = 4, scale = 1)
    private BigDecimal temperature;

    @Column(name = "humidity", precision = 4, scale = 1)
    private BigDecimal humidity;

    @Column(name = "precipitation", precision = 4, scale = 1)
    private BigDecimal precipitation;

    @Column(name = "wind", precision = 4, scale = 1)
    private BigDecimal wind;

    @ManyToOne
    @JoinColumn(name = "venue_id")
    private Venue venue;
}
