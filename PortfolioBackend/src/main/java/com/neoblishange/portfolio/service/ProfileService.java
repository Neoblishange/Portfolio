package com.neoblishange.portfolio.service;

import com.neoblishange.portfolio.dto.profile.ProfileRequestDTO;
import com.neoblishange.portfolio.dto.profile.ProfileResponseDTO;
import com.neoblishange.portfolio.entity.profile.Profile;
import com.neoblishange.portfolio.exception.ResourceNotFoundException;
import com.neoblishange.portfolio.mapper.ProfileMapper;
import com.neoblishange.portfolio.repository.ProfileRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProfileService {

    private final ProfileRepository profileRepository;
    private final ProfileMapper profileMapper;

    public ProfileService(
            ProfileRepository profileRepository,
            ProfileMapper profileMapper) {

        this.profileRepository = profileRepository;
        this.profileMapper = profileMapper;
    }

    @Transactional(readOnly = true)
    public ProfileResponseDTO getProfile() {

        Profile profile = profileRepository.findAll()
                .stream()
                .findFirst()
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Profile not found"
                        )
                );

        return profileMapper.toResponse(profile);
    }

    @Transactional
    public ProfileResponseDTO updateProfile(
            ProfileRequestDTO request) {

        Profile profile = profileRepository.findAll()
                .stream()
                .findFirst()
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Profile not found"
                        )
                );

        profileMapper.updateEntity(request, profile);

        return profileMapper.toResponse(profile);
    }
}