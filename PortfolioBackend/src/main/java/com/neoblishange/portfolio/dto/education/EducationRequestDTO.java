package com.neoblishange.portfolio.dto.education;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record EducationRequestDTO(

        @NotBlank
        @Size(max = 200)
        String schoolName,

        @Size(max = 150)
        String location,

        @NotNull
        LocalDate startDate,

        LocalDate endDate,

        @NotBlank
        @Size(max = 200)
        String degree,

        String description
) {
}