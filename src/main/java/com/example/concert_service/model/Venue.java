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
@Table(name = "venues")
public class Venue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "venue_id", nullable = false)
    private Integer venueId;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "city", length = 100)
    private String city;

    @Column(name = "latitude", precision = 9, scale = 6)
    private BigDecimal latitude;

    @Column(name = "longitude", precision = 9, scale = 6)
    private BigDecimal longitude;

    @Column(name = "capacity")
    private Integer capacity;

    @Column(name = "parking_available")
    private Boolean parkingAvailable;

    @OneToMany(mappedBy = "venue", cascade = CascadeType.ALL)
    private List<Concert> concerts;

    @OneToMany(mappedBy = "venue", cascade = CascadeType.ALL)
    private List<HotelVenue> hotelVenues;

    @OneToMany(mappedBy = "venue", cascade = CascadeType.ALL)
    private List<Transport> transports;

    @OneToMany(mappedBy = "venue", cascade = CascadeType.ALL)
    private List<Weather> weathers;

    @OneToMany(mappedBy = "venue", cascade = CascadeType.ALL)
    private List<Review> reviews;
}
