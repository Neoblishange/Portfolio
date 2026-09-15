package com.neoblishange.portfolio.dto.experience;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.List;

public record ExperienceRequestDTO(
        @NotBlank
        @Size(max = 150)
        String company,

        @NotBlank
        @Size(max = 150)
        String position,

        @NotBlank
        @Size(max = 150)
        String location,

        @NotBlank
        List<String> description,

        @NotNull
        LocalDate startDate,

        LocalDate endDate,

        @NotNull
        boolean current
) {
}
