package com.neoblishange.portfolio.service;

import com.neoblishange.portfolio.dto.experience.ExperienceRequestDTO;
import com.neoblishange.portfolio.dto.experience.ExperienceResponseDTO;
import com.neoblishange.portfolio.entity.Experience;
import com.neoblishange.portfolio.exception.ResourceNotFoundException;
import com.neoblishange.portfolio.mapper.ExperienceMapper;
import com.neoblishange.portfolio.repository.ExperienceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ExperienceService {
    private final ExperienceRepository experienceRepository;
    private final ExperienceMapper experienceMapper;

    public ExperienceService(ExperienceRepository experienceRepository,
                             ExperienceMapper experienceMapper) {
        this.experienceRepository = experienceRepository;
        this.experienceMapper = experienceMapper;
    }

    public List<ExperienceResponseDTO> getAllExperiences() {
        List<Experience> experiences = experienceRepository.findAll();
        return experiences
                .stream()
                .map(experienceMapper::toResponse)
                .toList();
    }

    public ExperienceResponseDTO getExperienceById(Long id) {
        Experience experience = experienceRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Experience with id " + id + " not found")
        );
        return experienceMapper.toResponse(experience);
    }

    public ExperienceResponseDTO createExperience(ExperienceRequestDTO experienceRequest) {
        Experience experience = experienceMapper.toEntity(experienceRequest);
        Experience savedExperience = experienceRepository.save(experience);
        return experienceMapper.toResponse(savedExperience);
    }

    @Transactional
    public ExperienceResponseDTO updateExperience(ExperienceRequestDTO experienceRequest, Long id) {
        Experience experience = experienceRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Experience with id " + id + " not found")
        );

        experienceMapper.updateEntity(experienceRequest, experience);

        return experienceMapper.toResponse(experience);
    }

    public void deleteExperience(Long id) {
        experienceRepository.deleteById(id);
    }
}
