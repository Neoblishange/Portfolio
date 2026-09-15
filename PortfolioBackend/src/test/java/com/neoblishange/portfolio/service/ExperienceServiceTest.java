package com.neoblishange.portfolio.service;

import com.neoblishange.portfolio.dto.experience.ExperienceRequestDTO;
import com.neoblishange.portfolio.dto.experience.ExperienceResponseDTO;
import com.neoblishange.portfolio.entity.Experience;
import com.neoblishange.portfolio.exception.ResourceNotFoundException;
import com.neoblishange.portfolio.mapper.ExperienceMapper;
import com.neoblishange.portfolio.repository.ExperienceRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExperienceServiceTest {

    @Mock
    private ExperienceRepository experienceRepository;

    @Mock
    private ExperienceMapper experienceMapper;

    @InjectMocks
    private ExperienceService experienceService;

    @Test
    void shouldReturnExperienceWhenExperienceExists() {

        // Arrange
        Long id = 1L;

        Experience experience = new Experience();
        experience.setId(id);
        experience.setCompany("Company");
        experience.setPosition("Java Developer");
        experience.setDescription(List.of("Backend development"));
        experience.setStartDate(LocalDate.of(2026, 1, 1));

        ExperienceResponseDTO response = new ExperienceResponseDTO(
                id,
                "Company",
                "Java Developer",
                null,
                List.of("Backend development"),
                LocalDate.of(2026, 1, 1),
                null,
                false
        );

        when(experienceRepository.findById(id))
                .thenReturn(Optional.of(experience));

        when(experienceMapper.toResponse(experience))
                .thenReturn(response);

        // Act
        ExperienceResponseDTO result =
                experienceService.getExperienceById(id);

        // Assert
        assertNotNull(result);
        assertEquals(response, result);

        verify(experienceRepository).findById(id);
        verify(experienceMapper).toResponse(experience);
    }

    @Test
    void shouldThrowResourceNotFoundExceptionWhenExperienceDoesNotExist() {

        // Arrange
        Long id = 999L;

        when(experienceRepository.findById(id))
                .thenReturn(Optional.empty());

        // Act + Assert
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> experienceService.getExperienceById(id)
        );

        assertEquals(
                "Experience with id " + id + " not found",
                exception.getMessage()
        );

        verify(experienceRepository).findById(id);
        verify(experienceMapper, never()).toResponse(any());
    }

    @Test
    void shouldReturnAllExperiences() {

        // Arrange
        Experience experience1 = new Experience();
        experience1.setId(1L);
        experience1.setCompany("Company A");
        experience1.setPosition("Java Developer");

        Experience experience2 = new Experience();
        experience2.setId(2L);
        experience2.setCompany("Company B");
        experience2.setPosition("Backend Developer");

        ExperienceResponseDTO response1 = new ExperienceResponseDTO(
                1L,
                "Company A",
                "Java Developer",
                null,
                List.of("Backend development"),
                LocalDate.of(2026, 1, 1),
                null,
                false
        );

        ExperienceResponseDTO response2 = new ExperienceResponseDTO(
                2L,
                "Company B",
                "Backend Developer",
                null,
                List.of("Backend development"),
                LocalDate.of(2025, 1, 1),
                LocalDate.of(2025, 12, 31),
                false
        );

        when(experienceRepository.findAll())
                .thenReturn(List.of(experience1, experience2));

        when(experienceMapper.toResponse(experience1))
                .thenReturn(response1);

        when(experienceMapper.toResponse(experience2))
                .thenReturn(response2);

        // Act
        List<ExperienceResponseDTO> result =
                experienceService.getAllExperiences();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());

        assertEquals(response1, result.get(0));
        assertEquals(response2, result.get(1));

        verify(experienceRepository).findAll();
        verify(experienceMapper).toResponse(experience1);
        verify(experienceMapper).toResponse(experience2);
    }

    @Test
    void shouldReturnEmptyListWhenNoExperienceExists() {

        // Arrange
        when(experienceRepository.findAll())
                .thenReturn(List.of());

        // Act
        List<ExperienceResponseDTO> result =
                experienceService.getAllExperiences();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(experienceRepository).findAll();
        verifyNoInteractions(experienceMapper);
    }

    @Test
    void shouldCreateExperienceSuccessfully() {

        // Arrange
        ExperienceRequestDTO request = new ExperienceRequestDTO(
                "Company",
                "Java Developer",
                null,
                List.of("Backend development"),
                LocalDate.of(2026, 1, 1),
                null,
                false
        );

        Experience experience = new Experience();

        Experience savedExperience = new Experience();
        savedExperience.setId(1L);
        savedExperience.setCompany("Company");
        savedExperience.setPosition("Java Developer");

        ExperienceResponseDTO response = new ExperienceResponseDTO(
                1L,
                "Company",
                "Java Developer",
                null,
                List.of("Backend development"),
                LocalDate.of(2026, 1, 1),
                null,
                false
        );

        when(experienceMapper.toEntity(request))
                .thenReturn(experience);

        when(experienceRepository.save(experience))
                .thenReturn(savedExperience);

        when(experienceMapper.toResponse(savedExperience))
                .thenReturn(response);

        // Act
        ExperienceResponseDTO result =
                experienceService.createExperience(request);

        // Assert
        assertNotNull(result);
        assertEquals(response, result);

        verify(experienceMapper).toEntity(request);
        verify(experienceRepository).save(experience);
        verify(experienceMapper).toResponse(savedExperience);
    }

    @Test
    void shouldUpdateExperienceSuccessfully() {

        // Arrange
        Long id = 1L;

        ExperienceRequestDTO request = new ExperienceRequestDTO(
                "Updated Company",
                "Senior Java Developer",
                "Paris",
                List.of("Updated description"),
                LocalDate.of(2026, 1, 1),
                null,
                true
        );

        Experience experience = new Experience();
        experience.setId(id);
        experience.setCompany("Company");
        experience.setPosition("Java Developer");

        ExperienceResponseDTO response = new ExperienceResponseDTO(
                id,
                "Updated Company",
                "Senior Java Developer",
                "Paris",
                List.of("Updated description"),
                LocalDate.of(2026, 1, 1),
                null,
                true
        );

        when(experienceRepository.findById(id))
                .thenReturn(Optional.of(experience));

        when(experienceMapper.toResponse(experience))
                .thenReturn(response);

        // Act
        ExperienceResponseDTO result =
                experienceService.updateExperience(request, id);

        // Assert
        assertNotNull(result);
        assertEquals(response, result);

        verify(experienceRepository).findById(id);
        verify(experienceMapper).updateEntity(request, experience);
        verify(experienceMapper).toResponse(experience);
    }

    @Test
    void shouldThrowResourceNotFoundExceptionWhenUpdatingNonExistingExperience() {

        // Arrange
        Long id = 999L;

        ExperienceRequestDTO request = new ExperienceRequestDTO(
                "Company",
                "Java Developer",
                null,
                List.of("Description"),
                LocalDate.of(2026, 1, 1),
                null,
                false
        );

        when(experienceRepository.findById(id))
                .thenReturn(Optional.empty());

        // Act + Assert
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> experienceService.updateExperience(request, id)
        );

        assertEquals(
                "Experience with id " + id + " not found",
                exception.getMessage()
        );

        verify(experienceRepository).findById(id);
        verify(experienceMapper, never()).updateEntity(any(), any());
        verify(experienceMapper, never()).toResponse(any());
    }

    @Test
    void shouldDeleteExperienceSuccessfully() {

        // Arrange
        Long id = 1L;

        // Act
        experienceService.deleteExperience(id);

        // Assert
        verify(experienceRepository).deleteById(id);
    }
}