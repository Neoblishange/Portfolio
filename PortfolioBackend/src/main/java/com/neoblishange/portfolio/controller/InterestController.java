package com.neoblishange.portfolio.controller;

import com.neoblishange.portfolio.dto.interest.InterestRequestDTO;
import com.neoblishange.portfolio.dto.interest.InterestResponseDTO;
import com.neoblishange.portfolio.service.InterestService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/interests")
public class InterestController {

    private final InterestService interestService;

    public InterestController(InterestService interestService) {
        this.interestService = interestService;
    }

    @GetMapping
    public ResponseEntity<List<InterestResponseDTO>> getAllInterests() {
        return ResponseEntity.ok(
                interestService.getAllInterests()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<InterestResponseDTO> getInterestById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                interestService.getInterestById(id)
        );
    }

    @PostMapping
    public ResponseEntity<InterestResponseDTO> createInterest(
            @Valid @RequestBody InterestRequestDTO request) {

        InterestResponseDTO response =
                interestService.createInterest(request);

        URI location =
                URI.create("/api/interests/" + response.id());

        return ResponseEntity
                .created(location)
                .body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<InterestResponseDTO> updateInterest(
            @PathVariable Long id,
            @Valid @RequestBody InterestRequestDTO request) {

        return ResponseEntity.ok(
                interestService.updateInterest(request, id)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInterest(
            @PathVariable Long id) {

        interestService.deleteInterest(id);

        return ResponseEntity.noContent().build();
    }
}