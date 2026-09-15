package com.neoblishange.portfolio.mapper;

import com.neoblishange.portfolio.dto.profile.ProfileRequestDTO;
import com.neoblishange.portfolio.dto.profile.ProfileResponseDTO;
import com.neoblishange.portfolio.entity.profile.Profile;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProfileMapper {

    Profile toEntity(ProfileRequestDTO request);

    ProfileResponseDTO toResponse(Profile profile);

    void updateEntity(
            ProfileRequestDTO request,
            @MappingTarget Profile profile
    );
}