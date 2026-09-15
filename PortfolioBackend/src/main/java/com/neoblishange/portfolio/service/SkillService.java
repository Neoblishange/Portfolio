package com.neoblishange.portfolio.service;

import com.neoblishange.portfolio.dto.skill.SkillRequestDTO;
import com.neoblishange.portfolio.dto.skill.SkillResponseDTO;
import com.neoblishange.portfolio.entity.Category;
import com.neoblishange.portfolio.entity.Skill;
import com.neoblishange.portfolio.exception.ResourceNotFoundException;
import com.neoblishange.portfolio.mapper.SkillMapper;
import com.neoblishange.portfolio.repository.CategoryRepository;
import com.neoblishange.portfolio.repository.SkillRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SkillService {
    private final SkillRepository skillRepository;
    private final SkillMapper skillMapper;
    private final CategoryRepository categoryRepository;

    public SkillService(SkillRepository skillRepository,
                        SkillMapper skillMapper,
                        CategoryRepository categoryRepository) {
        this.skillRepository = skillRepository;
        this.skillMapper = skillMapper;
        this.categoryRepository = categoryRepository;
    }

    public List<SkillResponseDTO> getAllSkills() {
        List<Skill> skills = skillRepository.findAll();
        return skills.stream()
                .map(skillMapper::toResponse)
                .toList();
    }

    public SkillResponseDTO getSkillById(Long id) {
        Skill skill = skillRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Skill with id " + id + " not found")
        );
        return skillMapper.toResponse(skill);
    }

    public SkillResponseDTO createSkill(SkillRequestDTO skillRequest) {
        Category category = categoryRepository
                .findById(skillRequest.categoryId())
                .orElseThrow(
                        () -> new ResourceNotFoundException("Category with id " + skillRequest.categoryId() + " not found")
                );

        Skill skill = skillMapper.toEntity(skillRequest);

        skill.setCategory(category);

        Skill savedSkill = skillRepository.save(skill);

        return skillMapper.toResponse(savedSkill);
    }

    @Transactional
    public SkillResponseDTO updateSkill(SkillRequestDTO skillRequest, Long id) {
        Category category = categoryRepository
                .findById(skillRequest.categoryId())
                .orElseThrow(
                        () -> new ResourceNotFoundException("Category with id " + skillRequest.categoryId() + " not found")
                );

        Skill skill = skillRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Skill with id " + id + " not found")
        );

        skill.setCategory(category);

        skillMapper.updateEntity(skillRequest, skill);

        return skillMapper.toResponse(skill);
    }

    public void deleteSkill(Long id) {
        skillRepository.deleteById(id);
    }
}
