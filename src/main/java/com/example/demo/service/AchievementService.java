package com.example.demo.service;

import com.example.demo.dto.AchievementDTO;
import com.example.demo.model.Achievement;
import com.example.demo.model.Society;
import com.example.demo.model.Council;
import com.example.demo.repository.AchievementRepository;
import com.example.demo.repository.SocietyRepository;
import com.example.demo.repository.CouncilRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AchievementService {

    private final AchievementRepository achievementRepository;
    private final SocietyRepository societyRepository;
    private final CouncilRepository councilRepository;

    public List<AchievementDTO> getAllAchievements() {
        log.info("Fetching all achievements");
        return achievementRepository.findAllByOrderByYearDesc()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Page<AchievementDTO> getAllAchievements(Pageable pageable) {
        log.info("Fetching achievements with pagination");
        return achievementRepository.findAllByOrderByYearDesc(pageable)
                .map(this::convertToDTO);
    }

    public Optional<AchievementDTO> getAchievementById(Long id) {
        log.info("Fetching achievement with id: {}", id);
        return achievementRepository.findById(id)
                .map(this::convertToDTO);
    }

    public List<AchievementDTO> getAchievementsByYear(String year) {
        log.info("Fetching achievements for year: {}", year);
        return achievementRepository.findByYearOrderByAchievementDateDesc(year)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<AchievementDTO> getFeaturedAchievements() {
        log.info("Fetching featured achievements");
        return achievementRepository.findByIsFeaturedTrueOrderByAchievementDateDesc()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<AchievementDTO> getAchievementsByCategory(String category) {
        log.info("Fetching achievements by category: {}", category);
        return achievementRepository.findByAwardCategory(category)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public AchievementDTO createAchievement(AchievementDTO achievementDTO) {
        log.info("Creating new achievement: {}", achievementDTO.getTitle());
        Achievement achievement = convertToEntity(achievementDTO);
        Achievement savedAchievement = achievementRepository.save(achievement);
        return convertToDTO(savedAchievement);
    }

    public Optional<AchievementDTO> updateAchievement(Long id, AchievementDTO achievementDTO) {
        log.info("Updating achievement with id: {}", id);
        return achievementRepository.findById(id)
                .map(existingAchievement -> {
                    updateAchievementFields(existingAchievement, achievementDTO);
                    Achievement updatedAchievement = achievementRepository.save(existingAchievement);
                    return convertToDTO(updatedAchievement);
                });
    }

    public boolean deleteAchievement(Long id) {
        log.info("Deleting achievement with id: {}", id);
        if (achievementRepository.existsById(id)) {
            achievementRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<AchievementDTO> searchAchievements(String keyword) {
        log.info("Searching achievements with keyword: {}", keyword);
        return achievementRepository.searchAchievementsByKeyword(keyword)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<Object[]> getAchievementCountByYear() {
        return achievementRepository.getAchievementCountByYear();
    }

    public List<Object[]> getAchievementCountByCategory() {
        return achievementRepository.getAchievementCountByCategory();
    }

    public long getFeaturedAchievementsCount() {
        return achievementRepository.countByIsFeaturedTrue();
    }

    private AchievementDTO convertToDTO(Achievement achievement) {
        return new AchievementDTO(
                achievement.getId(),
                achievement.getTitle(),
                achievement.getYear(),
                achievement.getDescription(),
                achievement.getImage(),
                achievement.getAwardCategory(),
                achievement.getAwardingOrganization(),
                achievement.getRecipientName(),
                achievement.getAchievementDate(),
                achievement.getIsFeatured(),
                achievement.getSociety() != null ? achievement.getSociety().getId() : null,
                achievement.getSociety() != null ? achievement.getSociety().getName() : null,
                achievement.getCouncil() != null ? achievement.getCouncil().getId() : null,
                achievement.getCouncil() != null ? achievement.getCouncil().getName() : null
        );
    }

    private Achievement convertToEntity(AchievementDTO achievementDTO) {
        Achievement achievement = new Achievement();
        updateAchievementFields(achievement, achievementDTO);
        return achievement;
    }

    private void updateAchievementFields(Achievement achievement, AchievementDTO achievementDTO) {
        achievement.setTitle(achievementDTO.getTitle());
        achievement.setYear(achievementDTO.getYear());
        achievement.setDescription(achievementDTO.getDescription());
        achievement.setImage(achievementDTO.getImage());
        achievement.setAwardCategory(achievementDTO.getAwardCategory());
        achievement.setAwardingOrganization(achievementDTO.getAwardingOrganization());
        achievement.setRecipientName(achievementDTO.getRecipientName());
        achievement.setAchievementDate(achievementDTO.getAchievementDate());
        achievement.setIsFeatured(achievementDTO.getIsFeatured() != null ? achievementDTO.getIsFeatured() : false);

        if (achievementDTO.getSocietyId() != null) {
            Society society = societyRepository.findById(achievementDTO.getSocietyId()).orElse(null);
            achievement.setSociety(society);
        }

        if (achievementDTO.getCouncilId() != null) {
            Council council = councilRepository.findById(achievementDTO.getCouncilId()).orElse(null);
            achievement.setCouncil(council);
        }
    }

    public List<AchievementDTO> getAchievementsBySocietyId(Long societyId) {
        log.info("Fetching achievements for society id: {}", societyId);
        Society society = societyRepository.findById(societyId).orElse(null);
        if (society == null) {
            return List.of();
        }
        return achievementRepository.findBySociety(society)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<AchievementDTO> getAchievementsByCouncilId(Long councilId) {
        log.info("Fetching achievements for council id: {}", councilId);
        Council council = councilRepository.findById(councilId).orElse(null);
        if (council == null) {
            return List.of();
        }
        return achievementRepository.findByCouncil(council)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
}
