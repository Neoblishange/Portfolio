package com.neoblishange.portfolio.dto.skill;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record SkillRequestDTO(
        @NotBlank
        @Size(max = 150)
        String name,

        @Size(max = 50)
        String level,

        @NotBlank
        Long categoryId,

        @NotNull
        Integer displayOrder
) { }
