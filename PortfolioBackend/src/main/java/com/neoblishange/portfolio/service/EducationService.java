package com.neoblishange.portfolio.service;

import com.neoblishange.portfolio.dto.education.EducationRequestDTO;
import com.neoblishange.portfolio.dto.education.EducationResponseDTO;
import com.neoblishange.portfolio.entity.Education;
import com.neoblishange.portfolio.exception.ResourceNotFoundException;
import com.neoblishange.portfolio.mapper.EducationMapper;
import com.neoblishange.portfolio.repository.EducationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EducationService {

    private final EducationRepository educationRepository;
    private final EducationMapper educationMapper;

    public EducationService(
            EducationRepository educationRepository,
            EducationMapper educationMapper) {

        this.educationRepository = educationRepository;
        this.educationMapper = educationMapper;
    }

    @Transactional(readOnly = true)
    public List<EducationResponseDTO> getAllEducations() {
        return educationRepository.findAll()
                .stream()
                .map(educationMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public EducationResponseDTO getEducationById(Long id) {

        Education education = educationRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Education not found with id: " + id
                        )
                );

        return educationMapper.toResponse(education);
    }

    @Transactional
    public EducationResponseDTO createEducation(
            EducationRequestDTO request) {

        Education education = educationMapper.toEntity(request);

        educationRepository.save(education);

        return educationMapper.toResponse(education);
    }

    @Transactional
    public EducationResponseDTO updateEducation(
            EducationRequestDTO request,
            Long id) {

        Education education = educationRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Education not found with id: " + id
                        )
                );

        educationMapper.updateEntity(request, education);

        return educationMapper.toResponse(education);
    }

    @Transactional
    public void deleteEducation(Long id) {

        if (!educationRepository.existsById(id)) {
            throw new ResourceNotFoundException(
                    "Education not found with id: " + id
            );
        }

        educationRepository.deleteById(id);
    }
}