package com.example.demo.service;

import com.example.demo.dto.CouncilDTO;
import com.example.demo.model.Council;
import com.example.demo.repository.CouncilRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CouncilService {

    private final CouncilRepository councilRepository;

    public List<CouncilDTO> getAllCouncils() {
        log.info("Fetching all councils");
        return councilRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<CouncilDTO> getActiveCouncils() {
        log.info("Fetching active councils");
        return councilRepository.findByIsActiveTrueOrderByNameAsc()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Optional<CouncilDTO> getCouncilById(Long id) {
        log.info("Fetching council with id: {}", id);
        return councilRepository.findById(id)
                .map(this::convertToDTO);
    }

    public Optional<CouncilDTO> getCouncilByName(String name) {
        log.info("Fetching council with name: {}", name);
        return councilRepository.findByNameIgnoreCase(name)
                .map(this::convertToDTO);
    }

    public CouncilDTO createCouncil(CouncilDTO councilDTO) {
        log.info("Creating new council: {}", councilDTO.getName());
        Council council = convertToEntity(councilDTO);
        Council savedCouncil = councilRepository.save(council);
        return convertToDTO(savedCouncil);
    }

    public Optional<CouncilDTO> updateCouncil(Long id, CouncilDTO councilDTO) {
        log.info("Updating council with id: {}", id);
        return councilRepository.findById(id)
                .map(existingCouncil -> {
                    updateCouncilFields(existingCouncil, councilDTO);
                    Council updatedCouncil = councilRepository.save(existingCouncil);
                    return convertToDTO(updatedCouncil);
                });
    }

    public boolean deleteCouncil(Long id) {
        log.info("Deleting council with id: {}", id);
        if (councilRepository.existsById(id)) {
            councilRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<CouncilDTO> searchCouncils(String keyword) {
        log.info("Searching councils with keyword: {}", keyword);
        return councilRepository.searchCouncilsByKeyword(keyword)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<CouncilDTO> getCouncilsWithWebsite() {
        log.info("Fetching councils with website");
        return councilRepository.findActiveCouncilsWithWebsite()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public long getActiveCouncilsCount() {
        return councilRepository.countByIsActiveTrue();
    }

    private CouncilDTO convertToDTO(Council council) {
        return new CouncilDTO(
                council.getId(),
                council.getName(),
                council.getImage(),
                council.getDescription(),
                council.getVision(),
                council.getMission(),
                council.getObjectives(),
                council.getSlateMembers(),
                council.getIsActive(),
                council.getChairPerson(),
                council.getMemberCount(),
                council.getStudentMemberCount(),
                council.getEstablishedYear(),
                council.getWebsiteUrl()
        );
    }

    private Council convertToEntity(CouncilDTO councilDTO) {
        Council council = new Council();
        updateCouncilFields(council, councilDTO);
        return council;
    }

    private void updateCouncilFields(Council council, CouncilDTO councilDTO) {
        council.setName(councilDTO.getName());
        council.setImage(councilDTO.getImage());
        council.setDescription(councilDTO.getDescription());
        council.setVision(councilDTO.getVision());
        council.setMission(councilDTO.getMission());
        council.setObjectives(councilDTO.getObjectives());
        council.setSlateMembers(councilDTO.getSlateMembers());
        council.setIsActive(councilDTO.getIsActive() != null ? councilDTO.getIsActive() : true);
        council.setChairPerson(councilDTO.getChairPerson());
        council.setMemberCount(councilDTO.getMemberCount() != null ? councilDTO.getMemberCount() : 0);
        council.setStudentMemberCount(councilDTO.getStudentMemberCount() != null ? councilDTO.getStudentMemberCount() : 0);
        council.setEstablishedYear(councilDTO.getEstablishedYear());
        council.setWebsiteUrl(councilDTO.getWebsiteUrl());
    }
}
