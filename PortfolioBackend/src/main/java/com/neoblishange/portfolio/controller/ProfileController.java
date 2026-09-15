package com.neoblishange.portfolio.controller;

import com.neoblishange.portfolio.dto.profile.ProfileRequestDTO;
import com.neoblishange.portfolio.dto.profile.ProfileResponseDTO;
import com.neoblishange.portfolio.service.ProfileService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {

    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping
    public ResponseEntity<ProfileResponseDTO> getProfile() {
        return ResponseEntity.ok(profileService.getProfile());
    }

    @PutMapping
    public ResponseEntity<ProfileResponseDTO> updateProfile(
            @Valid @RequestBody ProfileRequestDTO request) {

        return ResponseEntity.ok(
                profileService.updateProfile(request)
        );
    }
}