package com.example.demo.controller;

import com.example.demo.dto.CouncilDTO;
import com.example.demo.service.CouncilService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/councils")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class CouncilController {

    private final CouncilService councilService;

    @GetMapping
    public ResponseEntity<List<CouncilDTO>> getAllCouncils() {
        log.info("GET /api/councils - Fetching all councils");
        List<CouncilDTO> councils = councilService.getAllCouncils();
        return ResponseEntity.ok(councils);
    }

    @GetMapping("/active")
    public ResponseEntity<List<CouncilDTO>> getActiveCouncils() {
        log.info("GET /api/councils/active - Fetching active councils");
        List<CouncilDTO> activeCouncils = councilService.getActiveCouncils();
        return ResponseEntity.ok(activeCouncils);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CouncilDTO> getCouncilById(@PathVariable Long id) {
        log.info("GET /api/councils/{} - Fetching council by id", id);
        return councilService.getCouncilById(id)
                .map(council -> ResponseEntity.ok(council))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<CouncilDTO> getCouncilByName(@PathVariable String name) {
        log.info("GET /api/councils/name/{} - Fetching council by name", name);
        return councilService.getCouncilByName(name)
                .map(council -> ResponseEntity.ok(council))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search")
    public ResponseEntity<List<CouncilDTO>> searchCouncils(@RequestParam String keyword) {
        log.info("GET /api/councils/search?keyword={} - Searching councils", keyword);
        List<CouncilDTO> councils = councilService.searchCouncils(keyword);
        return ResponseEntity.ok(councils);
    }

    @GetMapping("/with-website")
    public ResponseEntity<List<CouncilDTO>> getCouncilsWithWebsite() {
        log.info("GET /api/councils/with-website - Fetching councils with website");
        List<CouncilDTO> councils = councilService.getCouncilsWithWebsite();
        return ResponseEntity.ok(councils);
    }

    @GetMapping("/count")
    public ResponseEntity<Long> getActiveCouncilsCount() {
        log.info("GET /api/councils/count - Fetching active councils count");
        long count = councilService.getActiveCouncilsCount();
        return ResponseEntity.ok(count);
    }

    @PostMapping
    public ResponseEntity<CouncilDTO> createCouncil(@RequestBody CouncilDTO councilDTO) {
        log.info("POST /api/councils - Creating new council: {}", councilDTO.getName());
        CouncilDTO createdCouncil = councilService.createCouncil(councilDTO);
        return new ResponseEntity<>(createdCouncil, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CouncilDTO> updateCouncil(@PathVariable Long id, @RequestBody CouncilDTO councilDTO) {
        log.info("PUT /api/councils/{} - Updating council", id);
        return councilService.updateCouncil(id, councilDTO)
                .map(updatedCouncil -> ResponseEntity.ok(updatedCouncil))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCouncil(@PathVariable Long id) {
        log.info("DELETE /api/councils/{} - Deleting council", id);
        boolean deleted = councilService.deleteCouncil(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
