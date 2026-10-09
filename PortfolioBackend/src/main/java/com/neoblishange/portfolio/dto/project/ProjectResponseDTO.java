package com.neoblishange.portfolio.dto.project;

import com.neoblishange.portfolio.dto.skill.SkillResponseDTO;
import com.neoblishange.portfolio.entity.project.ProjectContext;
import com.neoblishange.portfolio.entity.project.ProjectType;

import java.time.LocalDate;
import java.util.List;

public record ProjectResponseDTO(
        Long id,
        String title,
        String slug,
        ProjectType projectType,
        List<ProjectContext> projectContext,
        String shortDescription,
        List<String> description,
        List<String> functionalities,
        String imageUrl,
        LocalDate startDate,
        LocalDate endDate,
        List<SkillResponseDTO> skills
) { }
