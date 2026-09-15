package com.neoblishange.portfolio.dto.profile;

import com.neoblishange.portfolio.entity.profile.Availability;

public record ProfileResponseDTO(
        Long id,
        String firstName,
        String lastName,
        String jobTitle,
        String phone,
        String email,
        String linkedinUrl,
        String githubUrl,
        String pitch,
        Integer yearsOfExperience,
        Availability availability
) {
}