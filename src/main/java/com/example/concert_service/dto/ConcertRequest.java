package com.example.concert_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConcertRequest {

    @NotBlank(message = "El nombre del concierto es requerido")
    @Size(max = 255, message = "El nombre no puede exceder 255 caracteres")
    private String name;

    @NotNull(message = "La fecha y hora son requeridas")
    private LocalDateTime dateTime;

    @NotBlank(message = "El estado es requerido")
    @Size(max = 50)
    private String status;

    @NotNull(message = "El precio es requerido")
    @Positive(message = "El precio debe ser mayor a 0")
    private BigDecimal price;

    @NotNull(message = "El ID del venue es requerido")
    private Integer venueId;
}
