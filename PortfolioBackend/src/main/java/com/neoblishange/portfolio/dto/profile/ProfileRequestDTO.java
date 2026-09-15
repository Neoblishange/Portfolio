package com.neoblishange.portfolio.dto.profile;

import com.neoblishange.portfolio.entity.profile.Availability;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ProfileRequestDTO(

        @NotBlank
        @Size(max = 100)
        String firstName,

        @NotBlank
        @Size(max = 100)
        String lastName,

        @NotBlank
        @Size(max = 150)
        String jobTitle,

        @Size(max = 30)
        String phone,

        @NotBlank
        @Email
        @Size(max = 255)
        String email,

        @Size(max = 500)
        String linkedinUrl,

        @Size(max = 500)
        String githubUrl,

        @NotBlank
        String pitch,

        @NotNull
        @Min(0)
        @Max(100)
        Integer yearsOfExperience,

        @NotNull
        Availability availability
) {
}