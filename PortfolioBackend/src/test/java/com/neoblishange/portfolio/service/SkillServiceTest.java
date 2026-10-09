package com.neoblishange.portfolio.service;

import com.neoblishange.portfolio.dto.category.CategoryResponseDTO;
import com.neoblishange.portfolio.dto.skill.SkillRequestDTO;
import com.neoblishange.portfolio.dto.skill.SkillResponseDTO;
import com.neoblishange.portfolio.entity.Category;
import com.neoblishange.portfolio.entity.Skill;
import com.neoblishange.portfolio.exception.ResourceNotFoundException;
import com.neoblishange.portfolio.mapper.SkillMapper;
import com.neoblishange.portfolio.repository.CategoryRepository;
import com.neoblishange.portfolio.repository.SkillRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SkillServiceTest {

    @Mock
    private SkillRepository skillRepository;

    @Mock
    private SkillMapper skillMapper;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private SkillService skillService;

    @Test
    void shouldReturnSkillWhenSkillExists() {

        Long id = 1L;

        Category category = new Category();
        category.setId(id);
        category.setName("Backend");

        CategoryResponseDTO categoryResponse = new CategoryResponseDTO(
                id,
                "Backend"
        );

        Skill skill = new Skill();
        skill.setId(id);
        skill.setName("Java");
        skill.setFeatured(true);
        skill.setCategory(category);
        skill.setDisplayOrder(1);

        SkillResponseDTO response = new SkillResponseDTO(
                id,
                "Java",
                "1",
                true,
                categoryResponse,
                1
        );

        when(skillRepository.findById(id))
                .thenReturn(Optional.of(skill));

        when(skillMapper.toResponse(skill))
                .thenReturn(response);

        SkillResponseDTO result = skillService.getSkillById(id);

        assertNotNull(result);
        assertEquals(response, result);

        verify(skillRepository).findById(id);
        verify(skillMapper).toResponse(skill);
    }

    @Test
    void shouldThrowResourceNotFoundExceptionWhenSkillDoesNotExist() {

        Long id = 999L;

        when(skillRepository.findById(id))
                .thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> skillService.getSkillById(id)
        );

        assertEquals(
                "Skill with id " + id + " not found",
                exception.getMessage()
        );

        verify(skillRepository).findById(id);
        verify(skillMapper, never()).toResponse(any());
    }

    @Test
    void shouldReturnAllSkills() {

        Category category = new Category();
        category.setId(1L);
        category.setName("Backend");

        CategoryResponseDTO categoryResponse = new CategoryResponseDTO(
                1L,
                "Backend"
        );

        Skill skill1 = new Skill();
        skill1.setId(1L);
        skill1.setName("Java");
        skill1.setFeatured(true);
        skill1.setDisplayOrder(1);
        skill1.setCategory(category);

        Skill skill2 = new Skill();
        skill2.setId(2L);
        skill2.setName("Spring Boot");
        skill2.setFeatured(false);
        skill2.setDisplayOrder(2);
        skill2.setCategory(category);

        SkillResponseDTO response1 = new SkillResponseDTO(
                1L,
                "Java",
                "1",
                true,
                categoryResponse,
                1
        );

        SkillResponseDTO response2 = new SkillResponseDTO(
                2L,
                "Spring Boot",
                "2",
                false,
                categoryResponse,
                2
        );

        when(skillRepository.findAll())
                .thenReturn(List.of(skill1, skill2));

        when(skillMapper.toResponse(skill1))
                .thenReturn(response1);

        when(skillMapper.toResponse(skill2))
                .thenReturn(response2);

        List<SkillResponseDTO> result = skillService.getAllSkills();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(response1, result.get(0));
        assertEquals(response2, result.get(1));

        verify(skillRepository).findAll();
        verify(skillMapper).toResponse(skill1);
        verify(skillMapper).toResponse(skill2);
    }

    @Test
    void shouldReturnEmptyListWhenNoSkillExists() {

        when(skillRepository.findAll())
                .thenReturn(List.of());

        List<SkillResponseDTO> result = skillService.getAllSkills();

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(skillRepository).findAll();
        verifyNoInteractions(skillMapper);
    }

    @Test
    void shouldCreateSkillSuccessfully() {

        SkillRequestDTO request = new SkillRequestDTO(
                "Java",
                "1",
                true,
                1L,
                1
        );

        Category category = new Category();
        category.setId(1L);
        category.setName("Backend");

        CategoryResponseDTO categoryResponse = new CategoryResponseDTO(
                1L,
                "Backend"
        );

        Skill skill = new Skill();

        Skill savedSkill = new Skill();
        savedSkill.setId(1L);
        savedSkill.setName("Java");
        savedSkill.setLevel("1");
        savedSkill.setFeatured(true);
        savedSkill.setDisplayOrder(1);
        savedSkill.setCategory(category);

        SkillResponseDTO response = new SkillResponseDTO(
                1L,
                "Java",
                "1",
                true,
                categoryResponse,
                1
        );

        when(categoryRepository.findById(1L))
                .thenReturn(Optional.of(category));

        when(skillMapper.toEntity(request))
                .thenReturn(skill);

        when(skillRepository.save(skill))
                .thenReturn(savedSkill);

        when(skillMapper.toResponse(savedSkill))
                .thenReturn(response);

        SkillResponseDTO result = skillService.createSkill(request);

        assertNotNull(result);
        assertEquals(response, result);

        verify(categoryRepository).findById(1L);
        verify(skillMapper).toEntity(request);
        verify(skillRepository).save(skill);
        verify(skillMapper).toResponse(savedSkill);
        assertEquals(category, skill.getCategory());
    }

    @Test
    void shouldThrowResourceNotFoundExceptionWhenCreatingSkillWithNonExistingCategory() {
        SkillRequestDTO request = new SkillRequestDTO(
                "Java",
                "1",
                true,
                999L,
                1
        );

        when(categoryRepository.findById(999L))
                .thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> skillService.createSkill(request)
        );

        assertEquals(
                "Category with id 999 not found",
                exception.getMessage()
        );

        verify(categoryRepository).findById(999L);
        verifyNoInteractions(skillMapper);
        verifyNoInteractions(skillRepository);
    }

    @Test
    void shouldUpdateSkillSuccessfully() {

        Long skillId = 1L;
        Long categoryId = 2L;

        SkillRequestDTO request = new SkillRequestDTO(
                "Spring Boot",
                "2",
                true,
                categoryId,
                2
        );

        Category category = new Category();
        category.setId(categoryId);
        category.setName("Backend");

        CategoryResponseDTO categoryResponse = new CategoryResponseDTO(
                categoryId,
                "Backend"
        );

        Skill skill = new Skill();
        skill.setId(skillId);
        skill.setName("Spring");
        skill.setFeatured(false);
        skill.setDisplayOrder(1);

        SkillResponseDTO response = new SkillResponseDTO(
                skillId,
                "Spring Boot",
                "2",
                true,
                categoryResponse,
                2
        );

        when(categoryRepository.findById(categoryId))
                .thenReturn(Optional.of(category));

        when(skillRepository.findById(skillId))
                .thenReturn(Optional.of(skill));

        when(skillMapper.toResponse(skill))
                .thenReturn(response);

        SkillResponseDTO result = skillService.updateSkill(request, skillId);

        assertNotNull(result);
        assertEquals(response, result);

        verify(categoryRepository).findById(categoryId);
        verify(skillRepository).findById(skillId);
        verify(skillMapper).updateEntity(request, skill);
        verify(skillMapper).toResponse(skill);
        assertEquals(category, skill.getCategory());
    }

    @Test
    void shouldThrowResourceNotFoundExceptionWhenUpdatingSkillWithNonExistingCategory() {

        Long skillId = 1L;
        Long categoryId = 999L;

        SkillRequestDTO request = new SkillRequestDTO(
                "Spring Boot",
                "2",
                true,
                categoryId,
                2
        );

        when(categoryRepository.findById(categoryId))
                .thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> skillService.updateSkill(request, skillId)
        );

        assertEquals(
                "Category with id " + categoryId + " not found",
                exception.getMessage()
        );

        verify(categoryRepository).findById(categoryId);
        verify(skillRepository, never()).findById(any());
        verify(skillMapper, never()).updateEntity(any(), any());
        verify(skillMapper, never()).toResponse(any());
    }

    @Test
    void shouldThrowResourceNotFoundExceptionWhenUpdatingNonExistingSkill() {

        Long skillId = 999L;
        Long categoryId = 1L;

        SkillRequestDTO request = new SkillRequestDTO(
                "Spring Boot",
                "2",
                true,
                categoryId,
                2
        );

        Category category = new Category();
        category.setId(categoryId);

        when(categoryRepository.findById(categoryId))
                .thenReturn(Optional.of(category));

        when(skillRepository.findById(skillId))
                .thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> skillService.updateSkill(request, skillId)
        );

        assertEquals(
                "Skill with id " + skillId + " not found",
                exception.getMessage()
        );

        verify(categoryRepository).findById(categoryId);
        verify(skillRepository).findById(skillId);
        verify(skillMapper, never()).updateEntity(any(), any());
        verify(skillMapper, never()).toResponse(any());
    }

    @Test
    void shouldDeleteSkillSuccessfully() {

        Long id = 1L;

        skillService.deleteSkill(id);

        verify(skillRepository).deleteById(id);
    }
}