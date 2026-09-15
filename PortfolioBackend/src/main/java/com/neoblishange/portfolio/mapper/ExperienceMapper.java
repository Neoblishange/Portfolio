package com.neoblishange.portfolio.mapper;

import com.neoblishange.portfolio.dto.experience.ExperienceRequestDTO;
import com.neoblishange.portfolio.dto.experience.ExperienceResponseDTO;
import com.neoblishange.portfolio.entity.Experience;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ExperienceMapper {
    Experience toEntity(ExperienceRequestDTO experienceRequest);

    ExperienceResponseDTO toResponse(Experience experience);

    void updateEntity(
            ExperienceRequestDTO experienceRequest,
            @MappingTarget Experience experience
    );
}
