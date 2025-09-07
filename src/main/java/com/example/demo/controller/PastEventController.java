package com.example.demo.controller;

import com.example.demo.dto.PastEventDTO;
import com.example.demo.service.PastEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/past-events")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class PastEventController {

    private final PastEventService pastEventService;

    @GetMapping
    public ResponseEntity<List<PastEventDTO>> getAllPastEvents() {
        log.info("GET /api/past-events - Fetching all past events");
        List<PastEventDTO> events = pastEventService.getAllPastEvents();
        return ResponseEntity.ok(events);
    }

    @GetMapping("/paged")
    public ResponseEntity<Page<PastEventDTO>> getAllPastEvents(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        log.info("GET /api/past-events/paged - Fetching past events with pagination");
        Pageable pageable = PageRequest.of(page, size);
        Page<PastEventDTO> events = pastEventService.getAllPastEvents(pageable);
        return ResponseEntity.ok(events);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PastEventDTO> getPastEventById(@PathVariable Long id) {
        log.info("GET /api/past-events/{} - Fetching past event by id", id);
        return pastEventService.getPastEventById(id)
                .map(event -> ResponseEntity.ok(event))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<PastEventDTO>> getPastEventsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        log.info("GET /api/past-events/date-range - Fetching past events between {} and {}", startDate, endDate);
        List<PastEventDTO> events = pastEventService.getPastEventsByDateRange(startDate, endDate);
        return ResponseEntity.ok(events);
    }

    @GetMapping("/min-rating/{rating}")
    public ResponseEntity<List<PastEventDTO>> getEventsByMinRating(@PathVariable Double rating) {
        log.info("GET /api/past-events/min-rating/{} - Fetching events with minimum rating", rating);
        List<PastEventDTO> events = pastEventService.getEventsByMinRating(rating);
        return ResponseEntity.ok(events);
    }

    @GetMapping("/branch/{branchName}")
    public ResponseEntity<List<PastEventDTO>> getEventsByHostingBranch(@PathVariable String branchName) {
        log.info("GET /api/past-events/branch/{} - Fetching events by hosting branch", branchName);
        List<PastEventDTO> events = pastEventService.getEventsByHostingBranch(branchName);
        return ResponseEntity.ok(events);
    }

    @GetMapping("/search")
    public ResponseEntity<List<PastEventDTO>> searchEvents(@RequestParam String keyword) {
        log.info("GET /api/past-events/search?keyword={} - Searching past events", keyword);
        List<PastEventDTO> events = pastEventService.searchEvents(keyword);
        return ResponseEntity.ok(events);
    }

    @GetMapping("/statistics/by-year")
    public ResponseEntity<List<Object[]>> getEventCountByYear() {
        log.info("GET /api/past-events/statistics/by-year - Fetching event count by year");
        List<Object[]> statistics = pastEventService.getEventCountByYear();
        return ResponseEntity.ok(statistics);
    }

    @PostMapping
    public ResponseEntity<PastEventDTO> createPastEvent(@RequestBody PastEventDTO eventDTO) {
        log.info("POST /api/past-events - Creating new past event: {}", eventDTO.getTitle());
        PastEventDTO createdEvent = pastEventService.createPastEvent(eventDTO);
        return new ResponseEntity<>(createdEvent, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PastEventDTO> updatePastEvent(@PathVariable Long id, @RequestBody PastEventDTO eventDTO) {
        log.info("PUT /api/past-events/{} - Updating past event", id);
        return pastEventService.updatePastEvent(id, eventDTO)
                .map(updatedEvent -> ResponseEntity.ok(updatedEvent))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePastEvent(@PathVariable Long id) {
        log.info("DELETE /api/past-events/{} - Deleting past event", id);
        boolean deleted = pastEventService.deletePastEvent(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
