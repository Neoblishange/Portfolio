package com.neoblishange.portfolio.dto.interest;

import jakarta.validation.constraints.NotBlank;

public record InterestRequestDTO(

        @NotBlank
        String description
) {
}