package com.neoblishange.portfolio.mapper;

import com.neoblishange.portfolio.dto.interest.InterestRequestDTO;
import com.neoblishange.portfolio.dto.interest.InterestResponseDTO;
import com.neoblishange.portfolio.entity.Interest;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface InterestMapper {

    Interest toEntity(InterestRequestDTO request);

    InterestResponseDTO toResponse(Interest interest);

    void updateEntity(
            InterestRequestDTO request,
            @MappingTarget Interest interest
    );
}