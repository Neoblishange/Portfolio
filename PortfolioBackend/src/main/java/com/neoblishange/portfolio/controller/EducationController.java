package com.neoblishange.portfolio.controller;

import com.neoblishange.portfolio.dto.education.EducationRequestDTO;
import com.neoblishange.portfolio.dto.education.EducationResponseDTO;
import com.neoblishange.portfolio.service.EducationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/educations")
public class EducationController {

    private final EducationService educationService;

    public EducationController(EducationService educationService) {
        this.educationService = educationService;
    }

    @GetMapping
    public ResponseEntity<List<EducationResponseDTO>> getAllEducations() {
        return ResponseEntity.ok(
                educationService.getAllEducations()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<EducationResponseDTO> getEducationById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                educationService.getEducationById(id)
        );
    }

    @PostMapping
    public ResponseEntity<EducationResponseDTO> createEducation(
            @Valid @RequestBody EducationRequestDTO request) {

        EducationResponseDTO response =
                educationService.createEducation(request);

        URI location =
                URI.create("/api/educations/" + response.id());

        return ResponseEntity
                .created(location)
                .body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EducationResponseDTO> updateEducation(
            @PathVariable Long id,
            @Valid @RequestBody EducationRequestDTO request) {

        return ResponseEntity.ok(
                educationService.updateEducation(request, id)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEducation(
            @PathVariable Long id) {

        educationService.deleteEducation(id);

        return ResponseEntity.noContent().build();
    }
}