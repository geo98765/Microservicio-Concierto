package com.example.concert_service.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para horarios de apertura de un lugar
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OpeningHoursDto {
    private Boolean openNow;
    private List<String> weekdayText;
}
