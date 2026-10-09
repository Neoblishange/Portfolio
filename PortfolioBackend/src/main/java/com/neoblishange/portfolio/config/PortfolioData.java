package com.neoblishange.portfolio.config;

import com.neoblishange.portfolio.entity.Skill;
import com.neoblishange.portfolio.entity.profile.Availability;
import com.neoblishange.portfolio.entity.project.ProjectContext;
import com.neoblishange.portfolio.entity.project.ProjectType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record PortfolioData(
        ProfileData profile,
        List<CategoryData> categories,
        List<ExperienceData> experiences,
        List<EducationData> educations,
        List<InterestData> interests,
        List<ProjectData> projects
) {

    public record ProfileData(
            String firstName,
            String lastName,
            String jobTitle,
            String phone,
            String email,
            String linkedinUrl,
            String githubUrl,
            String pitch,
            BigDecimal yearsOfExperience,
            Availability availability
    ) {}

    public record CategoryData(
            String name,
            Integer displayOrder,
            List<SkillData> skills
    ) {}

    public record SkillData(
            String name,
            Integer displayOrder,
            String level,
            boolean featured
    ) {}

    public record ExperienceData(
            String company,
            String position,
            String location,
            LocalDate startDate,
            LocalDate endDate,
            boolean current,
            Integer displayOrder,
            List<String> description
    ) {}

    public record EducationData(
            String schoolName,
            String location,
            LocalDate startDate,
            LocalDate endDate,
            String degree,
            String description
    ) {}

    public record InterestData(
            String name,
            String description
    ) {}

    public record ProjectData(
            String title,
            String slug,
            ProjectType projectType,
            List<ProjectContext> projectContext,
            String shortDescription,
            List<String> description,
            List<String> functionalities,
            LocalDate startDate,
            LocalDate endDate,
            List<String> skills
    ) {}
}