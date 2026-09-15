package com.neoblishange.portfolio.mapper;

import com.neoblishange.portfolio.dto.project.ProjectRequestDTO;
import com.neoblishange.portfolio.dto.project.ProjectResponseDTO;
import com.neoblishange.portfolio.entity.Project;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProjectMapper {
    @Mapping(target = "images", ignore = true)
    Project toEntity(ProjectRequestDTO request);

    ProjectResponseDTO toResponse(Project project);

    @Mapping(target = "images", ignore = true)
    void updateEntity(
            ProjectRequestDTO request,
            @MappingTarget Project project
    );
}
