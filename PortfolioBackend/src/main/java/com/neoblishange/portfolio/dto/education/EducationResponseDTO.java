package com.neoblishange.portfolio.dto.education;

import java.time.LocalDate;

public record EducationResponseDTO(
        Long id,
        String schoolName,
        String location,
        LocalDate startDate,
        LocalDate endDate,
        String degree,
        String description
) {
}