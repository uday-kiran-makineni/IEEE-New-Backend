package com.example.demo.controller;

import com.example.demo.dto.UpcomingEventDTO;
import com.example.demo.model.UpcomingEvent;
import com.example.demo.service.UpcomingEventService;
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
@RequestMapping("/api/upcoming-events")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class UpcomingEventController {

    private final UpcomingEventService upcomingEventService;

    @GetMapping
    public ResponseEntity<List<UpcomingEventDTO>> getAllUpcomingEvents() {
        log.info("GET /api/upcoming-events - Fetching all upcoming events");
        List<UpcomingEventDTO> events = upcomingEventService.getAllUpcomingEvents();
        return ResponseEntity.ok(events);
    }

    @GetMapping("/paged")
    public ResponseEntity<Page<UpcomingEventDTO>> getAllUpcomingEvents(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        log.info("GET /api/upcoming-events/paged - Fetching upcoming events with pagination");
        Pageable pageable = PageRequest.of(page, size);
        Page<UpcomingEventDTO> events = upcomingEventService.getAllUpcomingEvents(pageable);
        return ResponseEntity.ok(events);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UpcomingEventDTO> getUpcomingEventById(@PathVariable Long id) {
        log.info("GET /api/upcoming-events/{} - Fetching upcoming event by id", id);
        return upcomingEventService.getUpcomingEventById(id)
                .map(event -> ResponseEntity.ok(event))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/after-date")
    public ResponseEntity<List<UpcomingEventDTO>> getUpcomingEventsAfterDate(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date) {
        log.info("GET /api/upcoming-events/after-date - Fetching upcoming events after {}", date);
        List<UpcomingEventDTO> events = upcomingEventService.getUpcomingEventsAfterDate(date);
        return ResponseEntity.ok(events);
    }

    @GetMapping("/open-registration")
    public ResponseEntity<List<UpcomingEventDTO>> getEventsWithOpenRegistration() {
        log.info("GET /api/upcoming-events/open-registration - Fetching events with open registration");
        List<UpcomingEventDTO> events = upcomingEventService.getEventsWithOpenRegistration();
        return ResponseEntity.ok(events);
    }

    @GetMapping("/free")
    public ResponseEntity<List<UpcomingEventDTO>> getFreeUpcomingEvents() {
        log.info("GET /api/upcoming-events/free - Fetching free upcoming events");
        List<UpcomingEventDTO> events = upcomingEventService.getFreeUpcomingEvents();
        return ResponseEntity.ok(events);
    }

    @GetMapping("/type/{eventType}")
    public ResponseEntity<List<UpcomingEventDTO>> getEventsByType(@PathVariable UpcomingEvent.EventType eventType) {
        log.info("GET /api/upcoming-events/type/{} - Fetching events by type", eventType);
        List<UpcomingEventDTO> events = upcomingEventService.getEventsByType(eventType);
        return ResponseEntity.ok(events);
    }

    @GetMapping("/search")
    public ResponseEntity<List<UpcomingEventDTO>> searchEvents(@RequestParam String keyword) {
        log.info("GET /api/upcoming-events/search?keyword={} - Searching upcoming events", keyword);
        List<UpcomingEventDTO> events = upcomingEventService.searchEvents(keyword);
        return ResponseEntity.ok(events);
    }

    @GetMapping("/count")
    public ResponseEntity<Long> getUpcomingEventsCount() {
        log.info("GET /api/upcoming-events/count - Fetching upcoming events count");
        long count = upcomingEventService.getUpcomingEventsCount();
        return ResponseEntity.ok(count);
    }

    @GetMapping("/open-registration/count")
    public ResponseEntity<Long> getOpenRegistrationEventsCount() {
        log.info("GET /api/upcoming-events/open-registration/count - Fetching open registration events count");
        long count = upcomingEventService.getOpenRegistrationEventsCount();
        return ResponseEntity.ok(count);
    }

    @PostMapping
    public ResponseEntity<UpcomingEventDTO> createUpcomingEvent(@RequestBody UpcomingEventDTO eventDTO) {
        log.info("POST /api/upcoming-events - Creating new upcoming event: {}", eventDTO.getTitle());
        UpcomingEventDTO createdEvent = upcomingEventService.createUpcomingEvent(eventDTO);
        return new ResponseEntity<>(createdEvent, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UpcomingEventDTO> updateUpcomingEvent(@PathVariable Long id, @RequestBody UpcomingEventDTO eventDTO) {
        log.info("PUT /api/upcoming-events/{} - Updating upcoming event", id);
        return upcomingEventService.updateUpcomingEvent(id, eventDTO)
                .map(updatedEvent -> ResponseEntity.ok(updatedEvent))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUpcomingEvent(@PathVariable Long id) {
        log.info("DELETE /api/upcoming-events/{} - Deleting upcoming event", id);
        boolean deleted = upcomingEventService.deleteUpcomingEvent(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
