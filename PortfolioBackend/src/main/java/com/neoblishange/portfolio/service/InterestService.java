package com.neoblishange.portfolio.service;

import com.neoblishange.portfolio.dto.interest.InterestRequestDTO;
import com.neoblishange.portfolio.dto.interest.InterestResponseDTO;
import com.neoblishange.portfolio.entity.Interest;
import com.neoblishange.portfolio.exception.ResourceNotFoundException;
import com.neoblishange.portfolio.mapper.InterestMapper;
import com.neoblishange.portfolio.repository.InterestRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class InterestService {

    private final InterestRepository interestRepository;
    private final InterestMapper interestMapper;

    public InterestService(
            InterestRepository interestRepository,
            InterestMapper interestMapper) {

        this.interestRepository = interestRepository;
        this.interestMapper = interestMapper;
    }

    @Transactional(readOnly = true)
    public List<InterestResponseDTO> getAllInterests() {
        return interestRepository.findAll()
                .stream()
                .map(interestMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public InterestResponseDTO getInterestById(Long id) {

        Interest interest = interestRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Interest not found with id: " + id
                        )
                );

        return interestMapper.toResponse(interest);
    }

    @Transactional
    public InterestResponseDTO createInterest(
            InterestRequestDTO request) {

        Interest interest = interestMapper.toEntity(request);

        interestRepository.save(interest);

        return interestMapper.toResponse(interest);
    }

    @Transactional
    public InterestResponseDTO updateInterest(
            InterestRequestDTO request,
            Long id) {

        Interest interest = interestRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Interest not found with id: " + id
                        )
                );

        interestMapper.updateEntity(request, interest);

        return interestMapper.toResponse(interest);
    }

    @Transactional
    public void deleteInterest(Long id) {

        if (!interestRepository.existsById(id)) {
            throw new ResourceNotFoundException(
                    "Interest not found with id: " + id
            );
        }

        interestRepository.deleteById(id);
    }
}