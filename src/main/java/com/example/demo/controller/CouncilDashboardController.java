package com.example.demo.controller;

import com.example.demo.dto.*;
import com.example.demo.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/council-dashboard")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class CouncilDashboardController {

    private final AuthService authService;
    private final CouncilService councilService;
    private final PastEventService pastEventService;
    private final UpcomingEventService upcomingEventService;
    private final AchievementService achievementService;
    private final GalleryItemService galleryItemService;

    private UserDTO validateCouncilAccess(String token, Long councilId) throws Exception {
        String cleanToken = token.replace("Bearer ", "");
        UserDTO user = authService.getUserFromToken(cleanToken).orElse(null);
        
        if (user == null) {
            throw new Exception("Invalid token");
        }
        
        // Check if user has access to this council
        if (!user.getRole().equals("COUNCIL_ADMIN") && !user.getRole().equals("ADMIN")) {
            throw new Exception("Insufficient permissions");
        }
        
        if (user.getRole().equals("COUNCIL_ADMIN") && !user.getEntityId().equals(councilId)) {
            throw new Exception("Access denied for this council");
        }
        
        return user;
    }

    @GetMapping("/council/{councilId}")
    public ResponseEntity<?> getCouncilDetails(
            @RequestHeader("Authorization") String token,
            @PathVariable Long councilId) {
        log.info("GET /api/council-dashboard/council/{} - Get council details", councilId);
        
        try {
            validateCouncilAccess(token, councilId);
            Optional<CouncilDTO> councilOpt = councilService.getCouncilById(councilId);
            if (!councilOpt.isPresent()) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Council not found");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
            }
            return ResponseEntity.ok(councilOpt.get());
        } catch (Exception e) {
            log.error("Failed to get council details", e);
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
        }
    }

    @PutMapping("/council/{councilId}")
    public ResponseEntity<?> updateCouncil(
            @RequestHeader("Authorization") String token,
            @PathVariable Long councilId,
            @RequestBody CouncilDTO councilDTO) {
        log.info("PUT /api/council-dashboard/council/{} - Update council", councilId);
        
        try {
            validateCouncilAccess(token, councilId);
            councilDTO.setId(councilId);
            Optional<CouncilDTO> updatedOpt = councilService.updateCouncil(councilId, councilDTO);
            if (!updatedOpt.isPresent()) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Council not found");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
            }
            return ResponseEntity.ok(updatedOpt.get());
        } catch (Exception e) {
            log.error("Failed to update council", e);
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
        }
    }

    @GetMapping("/council/{councilId}/events/past")
    public ResponseEntity<?> getCouncilPastEvents(
            @RequestHeader("Authorization") String token,
            @PathVariable Long councilId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        log.info("GET /api/council-dashboard/council/{}/events/past - Get council past events", councilId);
        
        try {
            validateCouncilAccess(token, councilId);
            List<PastEventDTO> events = pastEventService.getEventsByCouncilId(councilId);
            return ResponseEntity.ok(events);
        } catch (Exception e) {
            log.error("Failed to get council past events", e);
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
        }
    }

    @PostMapping("/council/{councilId}/events/past")
    public ResponseEntity<?> createPastEvent(
            @RequestHeader("Authorization") String token,
            @PathVariable Long councilId,
            @RequestBody PastEventDTO eventDTO) {
        log.info("POST /api/council-dashboard/council/{}/events/past - Create past event", councilId);
        
        try {
            validateCouncilAccess(token, councilId);
            eventDTO.setCouncilId(councilId);
            PastEventDTO created = pastEventService.createPastEvent(eventDTO);
            return new ResponseEntity<>(created, HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("Failed to create past event", e);
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    @PutMapping("/council/{councilId}/events/past/{eventId}")
    public ResponseEntity<?> updatePastEvent(
            @RequestHeader("Authorization") String token,
            @PathVariable Long councilId,
            @PathVariable Long eventId,
            @RequestBody PastEventDTO eventDTO) {
        log.info("PUT /api/council-dashboard/council/{}/events/past/{} - Update past event", councilId, eventId);
        
        try {
            validateCouncilAccess(token, councilId);
            eventDTO.setId(eventId);
            eventDTO.setCouncilId(councilId);
            Optional<PastEventDTO> updatedOpt = pastEventService.updatePastEvent(eventId, eventDTO);
            if (!updatedOpt.isPresent()) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Event not found");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
            }
            return ResponseEntity.ok(updatedOpt.get());
        } catch (Exception e) {
            log.error("Failed to update past event", e);
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    @DeleteMapping("/council/{councilId}/events/past/{eventId}")
    public ResponseEntity<?> deletePastEvent(
            @RequestHeader("Authorization") String token,
            @PathVariable Long councilId,
            @PathVariable Long eventId) {
        log.info("DELETE /api/council-dashboard/council/{}/events/past/{} - Delete past event", councilId, eventId);
        
        try {
            validateCouncilAccess(token, councilId);
            pastEventService.deletePastEvent(eventId);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Event deleted successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Failed to delete past event", e);
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    @GetMapping("/council/{councilId}/events/upcoming")
    public ResponseEntity<?> getCouncilUpcomingEvents(
            @RequestHeader("Authorization") String token,
            @PathVariable Long councilId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        log.info("GET /api/council-dashboard/council/{}/events/upcoming - Get council upcoming events", councilId);
        
        try {
            validateCouncilAccess(token, councilId);
            List<UpcomingEventDTO> events = upcomingEventService.getEventsByCouncilId(councilId);
            return ResponseEntity.ok(events);
        } catch (Exception e) {
            log.error("Failed to get council upcoming events", e);
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
        }
    }

    @PostMapping("/council/{councilId}/events/upcoming")
    public ResponseEntity<?> createUpcomingEvent(
            @RequestHeader("Authorization") String token,
            @PathVariable Long councilId,
            @RequestBody UpcomingEventDTO eventDTO) {
        log.info("POST /api/council-dashboard/council/{}/events/upcoming - Create upcoming event", councilId);
        
        try {
            validateCouncilAccess(token, councilId);
            eventDTO.setCouncilId(councilId);
            UpcomingEventDTO created = upcomingEventService.createUpcomingEvent(eventDTO);
            return new ResponseEntity<>(created, HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("Failed to create upcoming event", e);
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    @PutMapping("/council/{councilId}/events/upcoming/{eventId}")
    public ResponseEntity<?> updateUpcomingEvent(
            @RequestHeader("Authorization") String token,
            @PathVariable Long councilId,
            @PathVariable Long eventId,
            @RequestBody UpcomingEventDTO eventDTO) {
        log.info("PUT /api/council-dashboard/council/{}/events/upcoming/{} - Update upcoming event", councilId, eventId);
        
        try {
            validateCouncilAccess(token, councilId);
            eventDTO.setId(eventId);
            eventDTO.setCouncilId(councilId);
            Optional<UpcomingEventDTO> updatedOpt = upcomingEventService.updateUpcomingEvent(eventId, eventDTO);
            if (!updatedOpt.isPresent()) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Event not found");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
            }
            return ResponseEntity.ok(updatedOpt.get());
        } catch (Exception e) {
            log.error("Failed to update upcoming event", e);
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    @DeleteMapping("/council/{councilId}/events/upcoming/{eventId}")
    public ResponseEntity<?> deleteUpcomingEvent(
            @RequestHeader("Authorization") String token,
            @PathVariable Long councilId,
            @PathVariable Long eventId) {
        log.info("DELETE /api/council-dashboard/council/{}/events/upcoming/{} - Delete upcoming event", councilId, eventId);
        
        try {
            validateCouncilAccess(token, councilId);
            upcomingEventService.deleteUpcomingEvent(eventId);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Event deleted successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Failed to delete upcoming event", e);
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    @GetMapping("/council/{councilId}/achievements")
    public ResponseEntity<?> getCouncilAchievements(
            @RequestHeader("Authorization") String token,
            @PathVariable Long councilId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        log.info("GET /api/council-dashboard/council/{}/achievements - Get council achievements", councilId);
        
        try {
            validateCouncilAccess(token, councilId);
            List<AchievementDTO> achievements = achievementService.getAchievementsByCouncilId(councilId);
            return ResponseEntity.ok(achievements);
        } catch (Exception e) {
            log.error("Failed to get council achievements", e);
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
        }
    }

    @PostMapping("/council/{councilId}/achievements")
    public ResponseEntity<?> createAchievement(
            @RequestHeader("Authorization") String token,
            @PathVariable Long councilId,
            @RequestBody AchievementDTO achievementDTO) {
        log.info("POST /api/council-dashboard/council/{}/achievements - Create achievement", councilId);
        
        try {
            validateCouncilAccess(token, councilId);
            achievementDTO.setCouncilId(councilId);
            AchievementDTO created = achievementService.createAchievement(achievementDTO);
            return new ResponseEntity<>(created, HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("Failed to create achievement", e);
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    @PutMapping("/council/{councilId}/achievements/{achievementId}")
    public ResponseEntity<?> updateAchievement(
            @RequestHeader("Authorization") String token,
            @PathVariable Long councilId,
            @PathVariable Long achievementId,
            @RequestBody AchievementDTO achievementDTO) {
        log.info("PUT /api/council-dashboard/council/{}/achievements/{} - Update achievement", councilId, achievementId);
        
        try {
            validateCouncilAccess(token, councilId);
            achievementDTO.setId(achievementId);
            achievementDTO.setCouncilId(councilId);
            Optional<AchievementDTO> updatedOpt = achievementService.updateAchievement(achievementId, achievementDTO);
            if (!updatedOpt.isPresent()) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Achievement not found");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
            }
            return ResponseEntity.ok(updatedOpt.get());
        } catch (Exception e) {
            log.error("Failed to update achievement", e);
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    @DeleteMapping("/council/{councilId}/achievements/{achievementId}")
    public ResponseEntity<?> deleteAchievement(
            @RequestHeader("Authorization") String token,
            @PathVariable Long councilId,
            @PathVariable Long achievementId) {
        log.info("DELETE /api/council-dashboard/council/{}/achievements/{} - Delete achievement", councilId, achievementId);
        
        try {
            validateCouncilAccess(token, councilId);
            achievementService.deleteAchievement(achievementId);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Achievement deleted successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Failed to delete achievement", e);
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    @GetMapping("/council/{councilId}/gallery")
    public ResponseEntity<?> getCouncilGalleryItems(
            @RequestHeader("Authorization") String token,
            @PathVariable Long councilId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        log.info("GET /api/council-dashboard/council/{}/gallery - Get council gallery items", councilId);
        
        try {
            validateCouncilAccess(token, councilId);
            List<GalleryItemDTO> items = galleryItemService.getItemsByCouncilId(councilId);
            return ResponseEntity.ok(items);
        } catch (Exception e) {
            log.error("Failed to get council gallery items", e);
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
        }
    }

    @PostMapping("/council/{councilId}/gallery")
    public ResponseEntity<?> createGalleryItem(
            @RequestHeader("Authorization") String token,
            @PathVariable Long councilId,
            @RequestBody GalleryItemDTO itemDTO) {
        log.info("POST /api/council-dashboard/council/{}/gallery - Create gallery item", councilId);
        
        try {
            validateCouncilAccess(token, councilId);
            itemDTO.setCouncilId(councilId);
            GalleryItemDTO created = galleryItemService.createGalleryItem(itemDTO);
            return new ResponseEntity<>(created, HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("Failed to create gallery item", e);
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    @DeleteMapping("/council/{councilId}/gallery/{itemId}")
    public ResponseEntity<?> deleteGalleryItem(
            @RequestHeader("Authorization") String token,
            @PathVariable Long councilId,
            @PathVariable Long itemId) {
        log.info("DELETE /api/council-dashboard/council/{}/gallery/{} - Delete gallery item", councilId, itemId);
        
        try {
            validateCouncilAccess(token, councilId);
            galleryItemService.deleteGalleryItem(itemId);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Gallery item deleted successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Failed to delete gallery item", e);
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }
}
