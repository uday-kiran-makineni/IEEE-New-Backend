package com.example.demo.controller;

import com.example.demo.dto.HeroSlideDTO;
import com.example.demo.service.HeroSlideService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/hero-slides")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class HeroSlideController {

    private final HeroSlideService heroSlideService;

    @GetMapping
    public ResponseEntity<List<HeroSlideDTO>> getAllHeroSlides() {
        log.info("GET /api/hero-slides - Fetching all hero slides");
        List<HeroSlideDTO> heroSlides = heroSlideService.getAllHeroSlides();
        return ResponseEntity.ok(heroSlides);
    }

    @GetMapping("/active")
    public ResponseEntity<List<HeroSlideDTO>> getActiveHeroSlides() {
        log.info("GET /api/hero-slides/active - Fetching active hero slides");
        List<HeroSlideDTO> activeHeroSlides = heroSlideService.getActiveHeroSlides();
        return ResponseEntity.ok(activeHeroSlides);
    }

    @GetMapping("/{id}")
    public ResponseEntity<HeroSlideDTO> getHeroSlideById(@PathVariable Long id) {
        log.info("GET /api/hero-slides/{} - Fetching hero slide by id", id);
        return heroSlideService.getHeroSlideById(id)
                .map(heroSlide -> ResponseEntity.ok(heroSlide))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/count")
    public ResponseEntity<Long> getActiveHeroSlidesCount() {
        log.info("GET /api/hero-slides/count - Fetching active hero slides count");
        long count = heroSlideService.getActiveHeroSlidesCount();
        return ResponseEntity.ok(count);
    }

    @PostMapping
    public ResponseEntity<HeroSlideDTO> createHeroSlide(@RequestBody HeroSlideDTO heroSlideDTO) {
        log.info("POST /api/hero-slides - Creating new hero slide: {}", heroSlideDTO.getTitle());
        HeroSlideDTO createdHeroSlide = heroSlideService.createHeroSlide(heroSlideDTO);
        return new ResponseEntity<>(createdHeroSlide, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<HeroSlideDTO> updateHeroSlide(@PathVariable Long id, @RequestBody HeroSlideDTO heroSlideDTO) {
        log.info("PUT /api/hero-slides/{} - Updating hero slide", id);
        return heroSlideService.updateHeroSlide(id, heroSlideDTO)
                .map(updatedHeroSlide -> ResponseEntity.ok(updatedHeroSlide))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHeroSlide(@PathVariable Long id) {
        log.info("DELETE /api/hero-slides/{} - Deleting hero slide", id);
        boolean deleted = heroSlideService.deleteHeroSlide(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
