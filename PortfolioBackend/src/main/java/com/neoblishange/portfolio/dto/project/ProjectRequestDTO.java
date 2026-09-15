package com.neoblishange.portfolio.dto.project;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.List;

public record ProjectRequestDTO(
        @NotBlank
        @Size(max = 150)
        String title,

        @NotBlank
        @Size(max = 150)
        String slug,

        @NotBlank
        @Size(max = 255)
        String shortDescription,

        @NotBlank
        List<String> description,

        @NotNull
        LocalDate startDate,

        LocalDate endDate
) { }
