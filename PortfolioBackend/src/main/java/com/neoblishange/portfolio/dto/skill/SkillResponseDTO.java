package com.neoblishange.portfolio.dto.skill;

import com.neoblishange.portfolio.dto.category.CategoryResponseDTO;

public record SkillResponseDTO(
        Long id,
        String name,
        String level,
        CategoryResponseDTO category,
        Integer displayOrder
) {
}
