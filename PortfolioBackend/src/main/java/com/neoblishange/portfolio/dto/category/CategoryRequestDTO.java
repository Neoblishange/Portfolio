package com.neoblishange.portfolio.dto.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CategoryRequestDTO(
        @NotBlank
        @Size(max = 100)
        String name
) { }
