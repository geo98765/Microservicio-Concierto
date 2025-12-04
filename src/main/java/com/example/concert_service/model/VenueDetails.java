package com.example.concert_service.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class VenueDetails {
    private String name;
    private String address;
    private String phone;
    private String website;
    private String placeId;
    private Double rating;
    private Integer reviews;
    private Double latitude;
    private Double longitude;
    private String type;
    private String thumbnail;
}
