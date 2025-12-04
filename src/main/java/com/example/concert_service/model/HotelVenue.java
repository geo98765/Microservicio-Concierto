package com.example.concert_service.model;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "hotels_venues")
public class HotelVenue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hotel_venue_id", nullable = false)
    private Integer hotelVenueId;

    @Column(name = "name", length = 255)
    private String name;

    @Column(name = "location", length = 255)
    private String location;

    @Column(name = "rating", precision = 2, scale = 1)
    private BigDecimal rating;

    @Column(name = "distance", precision = 6, scale = 2)
    private BigDecimal distance;

    @ManyToOne
    @JoinColumn(name = "venue_id")
    private Venue venue;
}
