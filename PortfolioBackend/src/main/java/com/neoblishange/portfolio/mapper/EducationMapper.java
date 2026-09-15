package com.neoblishange.portfolio.mapper;

import com.neoblishange.portfolio.dto.education.EducationRequestDTO;
import com.neoblishange.portfolio.dto.education.EducationResponseDTO;
import com.neoblishange.portfolio.entity.Education;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface EducationMapper {

    Education toEntity(EducationRequestDTO request);

    EducationResponseDTO toResponse(Education education);

    void updateEntity(
            EducationRequestDTO request,
            @MappingTarget Education education
    );
}