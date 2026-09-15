package com.neoblishange.portfolio.controller;

import com.neoblishange.portfolio.dto.experience.ExperienceRequestDTO;
import com.neoblishange.portfolio.dto.experience.ExperienceResponseDTO;
import com.neoblishange.portfolio.service.ExperienceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(
        name = "Experiences",
        description = "Manage my experiences"
)
@RestController
@RequestMapping("/api/experiences")
public class ExperienceController {
    private final ExperienceService experienceService;

    public ExperienceController(ExperienceService experienceService) {
        this.experienceService = experienceService;
    }

    @Operation(
            summary = "Get all experiences"
    )
    @GetMapping
    public ResponseEntity<List<ExperienceResponseDTO>> getAllExperiences() {
        return ResponseEntity.ok(experienceService.getAllExperiences());
    }

    @Operation(
            summary = "Get one experience by id"
    )
    @GetMapping("/{id}")
    public ResponseEntity<ExperienceResponseDTO> getExperienceById(@PathVariable Long id) {
        return ResponseEntity.ok(experienceService.getExperienceById(id));
    }

    @Operation(
            summary = "Create an experience"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Experience successfully created"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid experience data"
            )
    })
    @PostMapping
    public ResponseEntity<ExperienceResponseDTO> createExperience(@RequestBody ExperienceRequestDTO experienceRequest) {
        ExperienceResponseDTO response = experienceService.createExperience(experienceRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(
            summary = "Update an experience"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Experience successfully updated"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid experience data"
            )
    })
    @PutMapping("/{id}")
    public ResponseEntity<ExperienceResponseDTO> updateExperience(
            @RequestBody ExperienceRequestDTO experienceRequest,
            @PathVariable Long id) {
        return ResponseEntity.ok(experienceService.updateExperience(experienceRequest, id));
    }

    @Operation(
            summary = "Delete an experience"
    )
    @DeleteMapping("/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteExperience(@PathVariable Long id) {
        experienceService.deleteExperience(id);
    }
}
