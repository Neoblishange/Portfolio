package com.neoblishange.portfolio.service;

import com.neoblishange.portfolio.dto.project.ProjectRequestDTO;
import com.neoblishange.portfolio.dto.project.ProjectResponseDTO;
import com.neoblishange.portfolio.entity.Project;
import com.neoblishange.portfolio.exception.ResourceAlreadyExistsException;
import com.neoblishange.portfolio.exception.ResourceNotFoundException;
import com.neoblishange.portfolio.mapper.ProjectMapper;
import com.neoblishange.portfolio.repository.ProjectRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;

    public ProjectService(ProjectRepository projectRepository,
                          ProjectMapper projectMapper) {
        this.projectRepository = projectRepository;
        this.projectMapper = projectMapper;
    }

    public List<ProjectResponseDTO> getAllProjects() {
        List<Project> projects = projectRepository.findAll();
        return projects
                .stream()
                .map(projectMapper::toResponse)
                .toList();
    }

    public ProjectResponseDTO getProjectById(Long id) {
        Project project = projectRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Project with id " + id + " not found")
        );
        return projectMapper.toResponse(project);
    }

    public ProjectResponseDTO createProject(ProjectRequestDTO projectRequest) {
        String projectSlug = projectRequest.slug();

        if(projectRepository.existsBySlug(projectSlug)) {
            throw new ResourceAlreadyExistsException("Project with slug " + projectSlug + " already exists");
        }

        Project project = projectMapper.toEntity(projectRequest);

        Project savedProject = projectRepository.save(project);

        return projectMapper.toResponse(savedProject);
    }

    @Transactional
    public ProjectResponseDTO updateProject(ProjectRequestDTO projectRequest, Long id) {
        Project project = projectRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Project with id " + id + " not found")
        );

        String projectSlug = projectRequest.slug();

        if(projectRepository.existsBySlug(projectSlug) && !Objects.equals(project.getSlug(), projectSlug)) {
            throw new ResourceAlreadyExistsException("Project with slug " + projectSlug + " already exists");
        }

        projectMapper.updateEntity(projectRequest, project);

        return projectMapper.toResponse(project);
    }

    public void deleteProject(Long id) {
        projectRepository.deleteById(id);
    }
}
