package com.neoblishange.portfolio.dto.project;

import java.time.LocalDate;
import java.util.List;

public record ProjectResponseDTO(
        Long id,
        String title,
        String slug,
        String shortDescription,
        List<String> description,
        String imageUrl,
        LocalDate startDate,
        LocalDate endDate
) { }
