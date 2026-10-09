package com.neoblishange.portfolio.dto.project;

import com.neoblishange.portfolio.entity.project.ProjectContext;
import com.neoblishange.portfolio.entity.project.ProjectType;
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

        @NotNull
        ProjectType projectType,

        @NotNull
        List<ProjectContext> projectContext,

        @NotBlank
        @Size(max = 255)
        String shortDescription,

        @NotBlank
        List<String> description,

        @NotBlank
        List<String> functionalities,

        @NotNull
        LocalDate startDate,

        LocalDate endDate,

        @NotNull
        List<Long> skillIds
) { }
