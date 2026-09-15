package com.neoblishange.portfolio.mapper;

import com.neoblishange.portfolio.dto.skill.SkillRequestDTO;
import com.neoblishange.portfolio.dto.skill.SkillResponseDTO;
import com.neoblishange.portfolio.entity.Skill;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface SkillMapper {
    Skill toEntity(SkillRequestDTO skillRequest);

    SkillResponseDTO toResponse(Skill skill);

    @Mapping(target = "category", ignore = true)
    void updateEntity(
            SkillRequestDTO skillRequest,
            @MappingTarget Skill skill
    );
}
