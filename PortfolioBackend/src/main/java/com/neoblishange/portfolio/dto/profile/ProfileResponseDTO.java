package com.neoblishange.portfolio.dto.profile;

import com.neoblishange.portfolio.entity.profile.Availability;

import java.math.BigDecimal;

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
        BigDecimal yearsOfExperience,
        Availability availability
) {
}