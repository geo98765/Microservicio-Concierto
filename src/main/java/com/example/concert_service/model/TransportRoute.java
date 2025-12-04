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
@Table(name = "transport_routes")
public class TransportRoute {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "route_id", nullable = false)
    private Integer routeId;

    @Column(name = "start_route", length = 255)
    private String startRoute;

    @Column(name = "end_route", length = 255)
    private String endRoute;

    @Column(name = "estimated_time", length = 50)
    private String estimatedTime;

    @Column(name = "approximate_cost", precision = 10, scale = 2)
    private BigDecimal approximateCost;

    @ManyToOne
    @JoinColumn(name = "transport_id")
    private Transport transport;
}
