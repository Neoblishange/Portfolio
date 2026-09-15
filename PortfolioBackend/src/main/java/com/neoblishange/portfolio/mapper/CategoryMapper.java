package com.neoblishange.portfolio.mapper;

import com.neoblishange.portfolio.dto.category.CategoryRequestDTO;
import com.neoblishange.portfolio.dto.category.CategoryResponseDTO;
import com.neoblishange.portfolio.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CategoryMapper {
    Category toEntity(CategoryRequestDTO categoryRequest);

    CategoryResponseDTO toResponse(Category category);

    void updateEntity(
            CategoryRequestDTO categoryRequest,
            @MappingTarget Category category
    );
}
