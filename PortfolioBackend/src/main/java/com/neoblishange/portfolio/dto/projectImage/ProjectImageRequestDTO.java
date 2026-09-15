package com.neoblishange.portfolio.dto.projectImage;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ProjectImageRequestDTO(
        @NotBlank
        @Size(max = 500)
        String imageUrl,

        @Size(max = 300)
        String altText
) { }
