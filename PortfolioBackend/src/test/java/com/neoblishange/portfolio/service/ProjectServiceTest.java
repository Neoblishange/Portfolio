package com.neoblishange.portfolio.service;

import com.neoblishange.portfolio.dto.project.ProjectRequestDTO;
import com.neoblishange.portfolio.dto.project.ProjectResponseDTO;
import com.neoblishange.portfolio.entity.project.Project;
import com.neoblishange.portfolio.entity.project.ProjectContext;
import com.neoblishange.portfolio.entity.project.ProjectType;
import com.neoblishange.portfolio.exception.ResourceAlreadyExistsException;
import com.neoblishange.portfolio.exception.ResourceNotFoundException;
import com.neoblishange.portfolio.mapper.ProjectMapper;
import com.neoblishange.portfolio.repository.ProjectRepository;
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
public class ProjectServiceTest {
    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private ProjectMapper projectMapper;

    @InjectMocks
    private ProjectService projectService;

    @Test
    public void shouldReturnProjectWhenProjectExists() {
        Long id = 1L;
        Project project = new Project();
        project.setId(id);

        ProjectResponseDTO response = new ProjectResponseDTO(
                id,
                "Portfolio",
                "portfolio",
                ProjectType.WEB,
                List.of(ProjectContext.PERSONAL),
                "Personal developer portfolio",
                List.of("Personal developer portfolio"),
                List.of("Build portfolio"),
                null,
                LocalDate.of(2026, 1, 1),
                null,
                null
        );

        when(projectRepository.findById(id))
                .thenReturn(Optional.of(project));

        when(projectMapper.toResponse(project))
                .thenReturn(response);

        ProjectResponseDTO result = projectService.getProjectById(id);

        assertNotNull(result);
        assertEquals(response, result);

        verify(projectRepository).findById(id);
        verify(projectMapper).toResponse(project);
    }

    @Test
    void shouldThrowResourceNotFoundExceptionWhenProjectDoesNotExist() {

        Long id = 999L;

        when(projectRepository.findById(id))
                .thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> projectService.getProjectById(id)
        );

        assertEquals(
                "Project with id " + id + " not found",
                exception.getMessage()
        );

        verify(projectRepository).findById(id);
        verify(projectMapper, never()).toResponse(any());
    }

    @Test
    void shouldReturnAllProjects() {

        Project project1 = new Project();
        project1.setId(1L);
        project1.setTitle("Portfolio");

        Project project2 = new Project();
        project2.setId(2L);
        project2.setTitle("E-commerce");

        ProjectResponseDTO response1 = new ProjectResponseDTO(
                1L,
                "Portfolio",
                "portfolio",
                ProjectType.WEB,
                List.of(ProjectContext.PERSONAL),
                "Personal developer portfolio",
                List.of("My portfolio project"),
                List.of("Build portfolio"),
                null,
                LocalDate.of(2026, 1, 1),
                null,
                null
        );

        ProjectResponseDTO response2 = new ProjectResponseDTO(
                2L,
                "E-commerce",
                "e-commerce",
                ProjectType.WEB,
                List.of(ProjectContext.PERSONAL),
                "E-commerce application",
                List.of("My e-commerce project"),
                List.of("Build storefront"),
                null,
                LocalDate.of(2026, 2, 1),
                null,
                null
        );

        when(projectRepository.findAll())
                .thenReturn(List.of(project1, project2));

        when(projectMapper.toResponse(project1))
                .thenReturn(response1);

        when(projectMapper.toResponse(project2))
                .thenReturn(response2);

        List<ProjectResponseDTO> result = projectService.getAllProjects();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(response1, result.get(0));
        assertEquals(response2, result.get(1));

        verify(projectRepository).findAll();
        verify(projectMapper).toResponse(project1);
        verify(projectMapper).toResponse(project2);
    }

    @Test
    void shouldReturnEmptyListWhenNoProjectExists() {

        when(projectRepository.findAll())
                .thenReturn(List.of());

        List<ProjectResponseDTO> result = projectService.getAllProjects();

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(projectRepository).findAll();
        verifyNoInteractions(projectMapper);
    }

    @Test
    void shouldCreateProjectSuccessfully() {

        ProjectRequestDTO request = new ProjectRequestDTO(
                "Portfolio",
                "portfolio",
                ProjectType.WEB,
                List.of(ProjectContext.PERSONAL),
                "Personal developer portfolio",
                List.of("My personal developer portfolio"),
                List.of("Build portfolio"),
                LocalDate.of(2026, 1, 1),
                null,
                List.of(1L)
        );

        Project project = new Project();

        Project savedProject = new Project();
        savedProject.setId(1L);
        savedProject.setTitle("Portfolio");
        savedProject.setSlug("portfolio");

        ProjectResponseDTO response = new ProjectResponseDTO(
                1L,
                "Portfolio",
                "portfolio",
                ProjectType.WEB,
                List.of(ProjectContext.PERSONAL),
                "Personal developer portfolio",
                List.of("My personal developer portfolio"),
                List.of("Build portfolio"),
                null,
                LocalDate.of(2026, 1, 1),
                null,
                null
        );

        when(projectRepository.existsBySlug("portfolio"))
                .thenReturn(false);

        when(projectMapper.toEntity(request))
                .thenReturn(project);

        when(projectRepository.save(project))
                .thenReturn(savedProject);

        when(projectMapper.toResponse(savedProject))
                .thenReturn(response);

        ProjectResponseDTO result = projectService.createProject(request);

        assertNotNull(result);
        assertEquals(response, result);

        verify(projectRepository).existsBySlug("portfolio");
        verify(projectMapper).toEntity(request);
        verify(projectRepository).save(project);
        verify(projectMapper).toResponse(savedProject);
    }

    @Test
    void shouldThrowResourceAlreadyExistsExceptionWhenSlugAlreadyExists() {

        ProjectRequestDTO request = new ProjectRequestDTO(
                "Portfolio",
                "portfolio",
                ProjectType.WEB,
                List.of(ProjectContext.PERSONAL),
                "Personal developer portfolio",
                List.of("My personal developer portfolio"),
                List.of("Build portfolio"),
                LocalDate.of(2026, 1, 1),
                null,
                List.of(1L)
        );

        when(projectRepository.existsBySlug("portfolio"))
                .thenReturn(true);

        ResourceAlreadyExistsException exception = assertThrows(
                ResourceAlreadyExistsException.class,
                () -> projectService.createProject(request)
        );

        assertEquals(
                "Project with slug portfolio already exists",
                exception.getMessage()
        );

        verify(projectRepository).existsBySlug("portfolio");
        verify(projectMapper, never()).toEntity(any());
        verify(projectRepository, never()).save(any());
        verify(projectMapper, never()).toResponse(any());
    }

    @Test
    void shouldUpdateProjectSuccessfully() {

        Long id = 1L;

        ProjectRequestDTO request = new ProjectRequestDTO(
                "Updated Portfolio",
                "updated-portfolio",
                ProjectType.WEB,
                List.of(ProjectContext.PERSONAL),
                "Updated short description",
                List.of("Updated description"),
                List.of("Update portfolio"),
                LocalDate.of(2026, 1, 1),
                null,
                List.of(1L)
        );

        Project project = new Project();
        project.setId(id);
        project.setTitle("Portfolio");
        project.setSlug("portfolio");

        ProjectResponseDTO response = new ProjectResponseDTO(
                id,
                "Updated Portfolio",
                "updated-portfolio",
                ProjectType.WEB,
                List.of(ProjectContext.PERSONAL),
                "Updated short description",
                List.of("Updated description"),
                List.of("Update portfolio"),
                null,
                LocalDate.of(2026, 1, 1),
                null,
                null
        );

        when(projectRepository.findById(id))
                .thenReturn(Optional.of(project));

        when(projectRepository.existsBySlug("updated-portfolio"))
                .thenReturn(false);

        when(projectMapper.toResponse(project))
                .thenReturn(response);

        ProjectResponseDTO result = projectService.updateProject(request, id);

        assertNotNull(result);
        assertEquals(response, result);

        verify(projectRepository).findById(id);
        verify(projectRepository).existsBySlug("updated-portfolio");
        verify(projectMapper).updateEntity(request, project);
        verify(projectMapper).toResponse(project);
    }

    @Test
    void shouldThrowResourceNotFoundExceptionWhenUpdatedProjectDoesNotExist() {

        Long id = 999L;

        ProjectRequestDTO request = new ProjectRequestDTO(
                "Updated Portfolio",
                "updated-portfolio",
                ProjectType.WEB,
                List.of(ProjectContext.PERSONAL),
                "Updated short description",
                List.of("Updated description"),
                List.of("Update portfolio"),
                LocalDate.of(2026, 1, 1),
                null,
                List.of(1L)
        );

        when(projectRepository.findById(id))
                .thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> projectService.updateProject(request, id)
        );

        assertEquals(
                "Project with id " + id + " not found",
                exception.getMessage()
        );

        verify(projectRepository).findById(id);
        verify(projectRepository, never()).existsBySlug(any());
        verify(projectMapper, never()).updateEntity(any(), any());
        verify(projectMapper, never()).toResponse(any());
    }

    @Test
    void shouldThrowResourceAlreadyExistsExceptionWhenUpdatedSlugAlreadyExists() {

        Long id = 1L;

        ProjectRequestDTO request = new ProjectRequestDTO(
                "E-commerce",
                "ecommerce",
                ProjectType.WEB,
                List.of(ProjectContext.PERSONAL),
                "E-commerce application",
                List.of("My e-commerce project"),
                List.of("Build storefront"),
                LocalDate.of(2026, 1, 1),
                null,
                List.of(1L)
        );

        Project project = new Project();
        project.setId(id);
        project.setTitle("Portfolio");
        project.setSlug("portfolio");

        when(projectRepository.findById(id))
                .thenReturn(Optional.of(project));

        when(projectRepository.existsBySlug("ecommerce"))
                .thenReturn(true);

        ResourceAlreadyExistsException exception = assertThrows(
                ResourceAlreadyExistsException.class,
                () -> projectService.updateProject(request, id)
        );

        assertEquals(
                "Project with slug ecommerce already exists",
                exception.getMessage()
        );

        verify(projectRepository).findById(id);
        verify(projectRepository).existsBySlug("ecommerce");
        verify(projectMapper, never()).updateEntity(any(), any());
        verify(projectMapper, never()).toResponse(any());
    }

    @Test
    void shouldAllowProjectToKeepItsCurrentSlug() {

        Long id = 1L;

        ProjectRequestDTO request = new ProjectRequestDTO(
                "Updated Portfolio",
                "portfolio",
                ProjectType.WEB,
                List.of(ProjectContext.PERSONAL),
                "Updated short description",
                List.of("Updated description"),
                List.of("Update portfolio"),
                LocalDate.of(2026, 1, 1),
                null,
                List.of(1L)
        );

        Project project = new Project();
        project.setId(id);
        project.setTitle("Portfolio");
        project.setSlug("portfolio");

        ProjectResponseDTO response = new ProjectResponseDTO(
                id,
                "Updated Portfolio",
                "portfolio",
                ProjectType.WEB,
                List.of(ProjectContext.PERSONAL),
                "Updated short description",
                List.of("Updated description"),
                List.of("Update portfolio"),
                null,
                LocalDate.of(2026, 1, 1),
                null,
                null
        );

        when(projectRepository.findById(id))
                .thenReturn(Optional.of(project));

        when(projectRepository.existsBySlug("portfolio"))
                .thenReturn(true);

        when(projectMapper.toResponse(project))
                .thenReturn(response);

        ProjectResponseDTO result = projectService.updateProject(request, id);

        assertNotNull(result);
        assertEquals(response, result);

        verify(projectRepository).findById(id);
        verify(projectRepository).existsBySlug("portfolio");
        verify(projectMapper).updateEntity(request, project);
        verify(projectMapper).toResponse(project);
    }

    @Test
    void shouldDeleteProjectSuccessfully() {

        Long id = 1L;

        projectService.deleteProject(id);

        verify(projectRepository).deleteById(id);
    }
}
