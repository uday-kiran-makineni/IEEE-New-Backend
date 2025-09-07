package com.example.demo.controller;

import com.example.demo.dto.SocietyDTO;
import com.example.demo.service.SocietyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/societies")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class SocietyController {

    private final SocietyService societyService;

    @GetMapping
    public ResponseEntity<List<SocietyDTO>> getAllSocieties() {
        log.info("GET /api/societies - Fetching all societies");
        List<SocietyDTO> societies = societyService.getAllSocieties();
        return ResponseEntity.ok(societies);
    }

    @GetMapping("/active")
    public ResponseEntity<List<SocietyDTO>> getActiveSocieties() {
        log.info("GET /api/societies/active - Fetching active societies");
        List<SocietyDTO> activeSocieties = societyService.getActiveSocieties();
        return ResponseEntity.ok(activeSocieties);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SocietyDTO> getSocietyById(@PathVariable Long id) {
        log.info("GET /api/societies/{} - Fetching society by id", id);
        return societyService.getSocietyById(id)
                .map(society -> ResponseEntity.ok(society))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<SocietyDTO> getSocietyByName(@PathVariable String name) {
        log.info("GET /api/societies/name/{} - Fetching society by name", name);
        return societyService.getSocietyByName(name)
                .map(society -> ResponseEntity.ok(society))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search")
    public ResponseEntity<List<SocietyDTO>> searchSocieties(@RequestParam String keyword) {
        log.info("GET /api/societies/search?keyword={} - Searching societies", keyword);
        List<SocietyDTO> societies = societyService.searchSocieties(keyword);
        return ResponseEntity.ok(societies);
    }

    @GetMapping("/min-members/{minMembers}")
    public ResponseEntity<List<SocietyDTO>> getSocietiesWithMinMembers(@PathVariable Integer minMembers) {
        log.info("GET /api/societies/min-members/{} - Fetching societies with minimum members", minMembers);
        List<SocietyDTO> societies = societyService.getSocietiesWithMinMembers(minMembers);
        return ResponseEntity.ok(societies);
    }

    @GetMapping("/count")
    public ResponseEntity<Long> getActiveSocietiesCount() {
        log.info("GET /api/societies/count - Fetching active societies count");
        long count = societyService.getActiveSocietiesCount();
        return ResponseEntity.ok(count);
    }

    @PostMapping
    public ResponseEntity<SocietyDTO> createSociety(@RequestBody SocietyDTO societyDTO) {
        log.info("POST /api/societies - Creating new society: {}", societyDTO.getName());
        SocietyDTO createdSociety = societyService.createSociety(societyDTO);
        return new ResponseEntity<>(createdSociety, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SocietyDTO> updateSociety(@PathVariable Long id, @RequestBody SocietyDTO societyDTO) {
        log.info("PUT /api/societies/{} - Updating society", id);
        return societyService.updateSociety(id, societyDTO)
                .map(updatedSociety -> ResponseEntity.ok(updatedSociety))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSociety(@PathVariable Long id) {
        log.info("DELETE /api/societies/{} - Deleting society", id);
        boolean deleted = societyService.deleteSociety(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
