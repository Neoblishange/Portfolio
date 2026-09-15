package com.neoblishange.portfolio.controller;

import com.neoblishange.portfolio.dto.project.ProjectRequestDTO;
import com.neoblishange.portfolio.dto.project.ProjectResponseDTO;
import com.neoblishange.portfolio.service.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@Tag(
        name = "Projects",
        description = "Manage my projects"
)
@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @Operation(
            summary = "Get all projects"
    )
    @GetMapping
    public ResponseEntity<List<ProjectResponseDTO>> getAllProjects() {
        return ResponseEntity.ok(projectService.getAllProjects());
    }

    @Operation(
            summary = "Get one project by id"
    )
    @GetMapping("/{id}")
    public ResponseEntity<ProjectResponseDTO> getProjectById(@PathVariable Long id) {
        return ResponseEntity.ok(projectService.getProjectById(id));
    }

    @Operation(
            summary = "Create a project"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Project successfully created"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid project data"
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "A project with this slug already exists"
            )
    })
    @PostMapping
    public ResponseEntity<ProjectResponseDTO> createProject(@Valid @RequestBody ProjectRequestDTO request) {
        ProjectResponseDTO response = projectService.createProject(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(
            summary = "Update a project"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Project successfully updated"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid project data"
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "A project with this slug already exists"
            )
    })
    @PutMapping("/{id}")
    public ResponseEntity<ProjectResponseDTO> updateProject(
            @Valid @RequestBody ProjectRequestDTO request,
            @PathVariable Long id) {
        return ResponseEntity.ok(projectService.updateProject(request, id));
    }

    @Operation(
            summary = "Delete a project"
    )
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProject(@PathVariable Long id) {
        projectService.deleteProject(id);
    }
}
