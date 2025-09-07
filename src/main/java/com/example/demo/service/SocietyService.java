package com.example.demo.service;

import com.example.demo.dto.SocietyDTO;
import com.example.demo.model.Society;
import com.example.demo.repository.SocietyRepository;
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
public class SocietyService {

    private final SocietyRepository societyRepository;

    public List<SocietyDTO> getAllSocieties() {
        log.info("Fetching all societies");
        return societyRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<SocietyDTO> getActiveSocieties() {
        log.info("Fetching active societies");
        return societyRepository.findByIsActiveTrueOrderByNameAsc()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Optional<SocietyDTO> getSocietyById(Long id) {
        log.info("Fetching society with id: {}", id);
        return societyRepository.findById(id)
                .map(this::convertToDTO);
    }

    public Optional<SocietyDTO> getSocietyByName(String name) {
        log.info("Fetching society with name: {}", name);
        return societyRepository.findByNameIgnoreCase(name)
                .map(this::convertToDTO);
    }

    public SocietyDTO createSociety(SocietyDTO societyDTO) {
        log.info("Creating new society: {}", societyDTO.getName());
        Society society = convertToEntity(societyDTO);
        Society savedSociety = societyRepository.save(society);
        return convertToDTO(savedSociety);
    }

    public Optional<SocietyDTO> updateSociety(Long id, SocietyDTO societyDTO) {
        log.info("Updating society with id: {}", id);
        return societyRepository.findById(id)
                .map(existingSociety -> {
                    updateSocietyFields(existingSociety, societyDTO);
                    Society updatedSociety = societyRepository.save(existingSociety);
                    return convertToDTO(updatedSociety);
                });
    }

    public boolean deleteSociety(Long id) {
        log.info("Deleting society with id: {}", id);
        if (societyRepository.existsById(id)) {
            societyRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<SocietyDTO> searchSocieties(String keyword) {
        log.info("Searching societies with keyword: {}", keyword);
        return societyRepository.searchSocietiesByKeyword(keyword)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<SocietyDTO> getSocietiesWithMinMembers(Integer minMembers) {
        log.info("Fetching societies with minimum {} members", minMembers);
        return societyRepository.findActiveSocietiesWithMinMembers(minMembers)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public long getActiveSocietiesCount() {
        return societyRepository.countByIsActiveTrue();
    }

    private SocietyDTO convertToDTO(Society society) {
        return new SocietyDTO(
                society.getId(),
                society.getName(),
                society.getImage(),
                society.getDescription(),
                society.getVision(),
                society.getMission(),
                society.getObjectives(),
                society.getSlateMembers(),
                society.getEvents(),
                society.getAchievements(),
                society.getIsActive(),
                society.getMemberCount(),
                society.getStudentMemberCount(),
                society.getEstablishedYear()
        );
    }

    private Society convertToEntity(SocietyDTO societyDTO) {
        Society society = new Society();
        updateSocietyFields(society, societyDTO);
        return society;
    }

    private void updateSocietyFields(Society society, SocietyDTO societyDTO) {
        society.setName(societyDTO.getName());
        society.setImage(societyDTO.getImage());
        society.setDescription(societyDTO.getDescription());
        society.setVision(societyDTO.getVision());
        society.setMission(societyDTO.getMission());
        society.setObjectives(societyDTO.getObjectives());
        society.setSlateMembers(societyDTO.getSlateMembers());
        society.setEvents(societyDTO.getEvents());
        society.setAchievements(societyDTO.getAchievements());
        society.setIsActive(societyDTO.getIsActive() != null ? societyDTO.getIsActive() : true);
        society.setMemberCount(societyDTO.getMemberCount() != null ? societyDTO.getMemberCount() : 0);
        society.setStudentMemberCount(societyDTO.getStudentMemberCount() != null ? societyDTO.getStudentMemberCount() : 0);
        society.setEstablishedYear(societyDTO.getEstablishedYear());
    }
}
