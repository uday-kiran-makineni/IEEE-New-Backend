package com.example.demo.controller;

import com.example.demo.dto.AchievementDTO;
import com.example.demo.service.AchievementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/achievements")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class AchievementController {

    private final AchievementService achievementService;

    @GetMapping
    public ResponseEntity<List<AchievementDTO>> getAllAchievements() {
        log.info("GET /api/achievements - Fetching all achievements");
        List<AchievementDTO> achievements = achievementService.getAllAchievements();
        return ResponseEntity.ok(achievements);
    }

    @GetMapping("/paged")
    public ResponseEntity<Page<AchievementDTO>> getAllAchievements(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        log.info("GET /api/achievements/paged - Fetching achievements with pagination");
        Pageable pageable = PageRequest.of(page, size);
        Page<AchievementDTO> achievements = achievementService.getAllAchievements(pageable);
        return ResponseEntity.ok(achievements);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AchievementDTO> getAchievementById(@PathVariable Long id) {
        log.info("GET /api/achievements/{} - Fetching achievement by id", id);
        return achievementService.getAchievementById(id)
                .map(achievement -> ResponseEntity.ok(achievement))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/year/{year}")
    public ResponseEntity<List<AchievementDTO>> getAchievementsByYear(@PathVariable String year) {
        log.info("GET /api/achievements/year/{} - Fetching achievements by year", year);
        List<AchievementDTO> achievements = achievementService.getAchievementsByYear(year);
        return ResponseEntity.ok(achievements);
    }

    @GetMapping("/featured")
    public ResponseEntity<List<AchievementDTO>> getFeaturedAchievements() {
        log.info("GET /api/achievements/featured - Fetching featured achievements");
        List<AchievementDTO> achievements = achievementService.getFeaturedAchievements();
        return ResponseEntity.ok(achievements);
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<AchievementDTO>> getAchievementsByCategory(@PathVariable String category) {
        log.info("GET /api/achievements/category/{} - Fetching achievements by category", category);
        List<AchievementDTO> achievements = achievementService.getAchievementsByCategory(category);
        return ResponseEntity.ok(achievements);
    }

    @GetMapping("/search")
    public ResponseEntity<List<AchievementDTO>> searchAchievements(@RequestParam String keyword) {
        log.info("GET /api/achievements/search?keyword={} - Searching achievements", keyword);
        List<AchievementDTO> achievements = achievementService.searchAchievements(keyword);
        return ResponseEntity.ok(achievements);
    }

    @GetMapping("/statistics/by-year")
    public ResponseEntity<List<Object[]>> getAchievementCountByYear() {
        log.info("GET /api/achievements/statistics/by-year - Fetching achievement count by year");
        List<Object[]> statistics = achievementService.getAchievementCountByYear();
        return ResponseEntity.ok(statistics);
    }

    @GetMapping("/statistics/by-category")
    public ResponseEntity<List<Object[]>> getAchievementCountByCategory() {
        log.info("GET /api/achievements/statistics/by-category - Fetching achievement count by category");
        List<Object[]> statistics = achievementService.getAchievementCountByCategory();
        return ResponseEntity.ok(statistics);
    }

    @GetMapping("/count/featured")
    public ResponseEntity<Long> getFeaturedAchievementsCount() {
        log.info("GET /api/achievements/count/featured - Fetching featured achievements count");
        long count = achievementService.getFeaturedAchievementsCount();
        return ResponseEntity.ok(count);
    }

    @PostMapping
    public ResponseEntity<AchievementDTO> createAchievement(@RequestBody AchievementDTO achievementDTO) {
        log.info("POST /api/achievements - Creating new achievement: {}", achievementDTO.getTitle());
        AchievementDTO createdAchievement = achievementService.createAchievement(achievementDTO);
        return new ResponseEntity<>(createdAchievement, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AchievementDTO> updateAchievement(@PathVariable Long id, @RequestBody AchievementDTO achievementDTO) {
        log.info("PUT /api/achievements/{} - Updating achievement", id);
        return achievementService.updateAchievement(id, achievementDTO)
                .map(updatedAchievement -> ResponseEntity.ok(updatedAchievement))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAchievement(@PathVariable Long id) {
        log.info("DELETE /api/achievements/{} - Deleting achievement", id);
        boolean deleted = achievementService.deleteAchievement(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
