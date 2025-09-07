package com.example.demo.service;

import com.example.demo.dto.HeroSlideDTO;
import com.example.demo.model.HeroSlide;
import com.example.demo.repository.HeroSlideRepository;
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
public class HeroSlideService {

    private final HeroSlideRepository heroSlideRepository;

    public List<HeroSlideDTO> getAllHeroSlides() {
        log.info("Fetching all hero slides");
        return heroSlideRepository.findAllByOrderByDisplayOrderAsc()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<HeroSlideDTO> getActiveHeroSlides() {
        log.info("Fetching active hero slides");
        return heroSlideRepository.findByIsActiveTrueOrderByDisplayOrderAsc()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Optional<HeroSlideDTO> getHeroSlideById(Long id) {
        log.info("Fetching hero slide with id: {}", id);
        return heroSlideRepository.findById(id)
                .map(this::convertToDTO);
    }

    public HeroSlideDTO createHeroSlide(HeroSlideDTO heroSlideDTO) {
        log.info("Creating new hero slide: {}", heroSlideDTO.getTitle());
        HeroSlide heroSlide = convertToEntity(heroSlideDTO);
        
        // Set display order if not provided
        if (heroSlide.getDisplayOrder() == null) {
            Integer maxOrder = heroSlideRepository.findMaxDisplayOrder();
            heroSlide.setDisplayOrder(maxOrder != null ? maxOrder + 1 : 1);
        }
        
        HeroSlide savedHeroSlide = heroSlideRepository.save(heroSlide);
        return convertToDTO(savedHeroSlide);
    }

    public Optional<HeroSlideDTO> updateHeroSlide(Long id, HeroSlideDTO heroSlideDTO) {
        log.info("Updating hero slide with id: {}", id);
        return heroSlideRepository.findById(id)
                .map(existingHeroSlide -> {
                    updateHeroSlideFields(existingHeroSlide, heroSlideDTO);
                    HeroSlide updatedHeroSlide = heroSlideRepository.save(existingHeroSlide);
                    return convertToDTO(updatedHeroSlide);
                });
    }

    public boolean deleteHeroSlide(Long id) {
        log.info("Deleting hero slide with id: {}", id);
        if (heroSlideRepository.existsById(id)) {
            heroSlideRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public long getActiveHeroSlidesCount() {
        return heroSlideRepository.countByIsActiveTrue();
    }

    private HeroSlideDTO convertToDTO(HeroSlide heroSlide) {
        return new HeroSlideDTO(
                heroSlide.getId(),
                heroSlide.getTitle(),
                heroSlide.getSubtitle(),
                heroSlide.getImage(),
                heroSlide.getDescription(),
                heroSlide.getDisplayOrder(),
                heroSlide.getIsActive(),
                heroSlide.getButtonText(),
                heroSlide.getButtonUrl(),
                heroSlide.getBackgroundColor(),
                heroSlide.getTextColor()
        );
    }

    private HeroSlide convertToEntity(HeroSlideDTO heroSlideDTO) {
        HeroSlide heroSlide = new HeroSlide();
        updateHeroSlideFields(heroSlide, heroSlideDTO);
        return heroSlide;
    }

    private void updateHeroSlideFields(HeroSlide heroSlide, HeroSlideDTO heroSlideDTO) {
        heroSlide.setTitle(heroSlideDTO.getTitle());
        heroSlide.setSubtitle(heroSlideDTO.getSubtitle());
        heroSlide.setImage(heroSlideDTO.getImage());
        heroSlide.setDescription(heroSlideDTO.getDescription());
        heroSlide.setDisplayOrder(heroSlideDTO.getDisplayOrder());
        heroSlide.setIsActive(heroSlideDTO.getIsActive() != null ? heroSlideDTO.getIsActive() : true);
        heroSlide.setButtonText(heroSlideDTO.getButtonText());
        heroSlide.setButtonUrl(heroSlideDTO.getButtonUrl());
        heroSlide.setBackgroundColor(heroSlideDTO.getBackgroundColor());
        heroSlide.setTextColor(heroSlideDTO.getTextColor());
    }
}
