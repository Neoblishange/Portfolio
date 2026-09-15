package com.neoblishange.portfolio.controller;

import com.neoblishange.portfolio.dto.skill.SkillRequestDTO;
import com.neoblishange.portfolio.dto.skill.SkillResponseDTO;
import com.neoblishange.portfolio.service.SkillService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(
        name = "Skills",
        description = "Manage my skills"
)
@RestController
@RequestMapping("/api/skills")
public class SkillController {
    private final SkillService skillService;

    public SkillController(SkillService skillService) {
        this.skillService = skillService;
    }

    @Operation(
            summary = "Get all skills"
    )
    @GetMapping
    public ResponseEntity<List<SkillResponseDTO>> getAllSkills() {
        return ResponseEntity.ok(skillService.getAllSkills());
    }

    @Operation(
            summary = "Get one skill by id"
    )
    @GetMapping("/{id}")
    public ResponseEntity<SkillResponseDTO> getSkillById(@PathVariable Long id) {
        return ResponseEntity.ok(skillService.getSkillById(id));
    }

    @Operation(
            summary = "Create a skill"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Skill successfully created"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid skill data"
            )
    })
    @PostMapping
    public ResponseEntity<SkillResponseDTO> createSkill(@RequestBody SkillRequestDTO skillRequest) {
        SkillResponseDTO response = skillService.createSkill(skillRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(
            summary = "Update a skill"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Skill successfully updated"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid skill data"
            )
    })
    @PutMapping("/{id}")
    public ResponseEntity<SkillResponseDTO> updateSkill(
            @RequestBody SkillRequestDTO skillRequest,
            @PathVariable Long id) {
        return ResponseEntity.ok(skillService.updateSkill(skillRequest, id));
    }

    @Operation(
            summary = "Delete a skill"
    )
    @DeleteMapping("/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteSkill(@PathVariable Long id) {
        skillService.deleteSkill(id);
    }

}
