package com.neoblishange.portfolio.dto.experience;

import java.time.LocalDate;
import java.util.List;

public record ExperienceResponseDTO(
        Long id,
        String company,
        String position,
        String location,
        List<String> description,
        LocalDate startDate,
        LocalDate endDate,
        boolean current
){ }
