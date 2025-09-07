package com.example.demo.controller;

import com.example.demo.dto.*;
import com.example.demo.model.User;
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
@RequestMapping("/api/society-dashboard")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class SocietyDashboardController {

    private final AuthService authService;
    private final SocietyService societyService;
    private final PastEventService pastEventService;
    private final UpcomingEventService upcomingEventService;
    private final AchievementService achievementService;
    private final GalleryItemService galleryItemService;

    private UserDTO validateSocietyAccess(String token, Long societyId) throws Exception {
        log.info("=== VALIDATING SOCIETY ACCESS ===");
        log.info("Token: {}", token);
        log.info("Society ID: {}", societyId);
        
        String cleanToken = token.replace("Bearer ", "");
        log.info("Clean Token: {}", cleanToken);
        
        UserDTO user = authService.getUserFromToken(cleanToken).orElse(null);
        
        if (user == null) {
            log.error("Invalid token - user not found");
            throw new Exception("Invalid token");
        }
        
        log.info("User found: email={}, role={}, entityId={}", user.getEmail(), user.getRole(), user.getEntityId());
        
        // Check if user has access to this society
        if (user.getRole() != User.UserRole.SOCIETY_ADMIN && user.getRole() != User.UserRole.ADMIN) {
            log.error("Insufficient permissions - user role: {}", user.getRole());
            throw new Exception("Insufficient permissions");
        }
        
        if (user.getRole() == User.UserRole.SOCIETY_ADMIN) {
            log.info("Checking SOCIETY_ADMIN access - user.entityId={}, societyId={}", user.getEntityId(), societyId);
            if (!user.getEntityId().equals(societyId)) {
                log.error("Access denied - entityId mismatch: user.entityId={}, societyId={}", user.getEntityId(), societyId);
                throw new Exception("Access denied for this society");
            }
        }
        
        log.info("Access validation successful for user: {}", user.getEmail());
        return user;
    }

    @GetMapping("/society/{societyId}")
    public ResponseEntity<?> getSocietyDetails(
            @RequestHeader("Authorization") String token,
            @PathVariable Long societyId) {
        log.info("GET /api/society-dashboard/society/{} - Get society details", societyId);
        
        try {
            validateSocietyAccess(token, societyId);
            Optional<SocietyDTO> societyOpt = societyService.getSocietyById(societyId);
            if (!societyOpt.isPresent()) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Society not found");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
            }
            return ResponseEntity.ok(societyOpt.get());
        } catch (Exception e) {
            log.error("Failed to get society details", e);
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
        }
    }

    @PutMapping("/society/{societyId}")
    public ResponseEntity<?> updateSociety(
            @RequestHeader("Authorization") String token,
            @PathVariable Long societyId,
            @RequestBody SocietyDTO societyDTO) {
        log.info("PUT /api/society-dashboard/society/{} - Update society", societyId);
        
        try {
            validateSocietyAccess(token, societyId);
            societyDTO.setId(societyId);
            Optional<SocietyDTO> updatedOpt = societyService.updateSociety(societyId, societyDTO);
            if (!updatedOpt.isPresent()) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Society not found");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
            }
            return ResponseEntity.ok(updatedOpt.get());
        } catch (Exception e) {
            log.error("Failed to update society", e);
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
        }
    }

    @GetMapping("/society/{societyId}/events/past")
    public ResponseEntity<?> getSocietyPastEvents(
            @RequestHeader("Authorization") String token,
            @PathVariable Long societyId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        log.info("GET /api/society-dashboard/society/{}/events/past - Get society past events", societyId);
        
        try {
            validateSocietyAccess(token, societyId);
            List<PastEventDTO> events = pastEventService.getEventsBySocietyId(societyId);
            return ResponseEntity.ok(events);
        } catch (Exception e) {
            log.error("Failed to get society past events", e);
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
        }
    }

    @PostMapping("/society/{societyId}/events/past")
    public ResponseEntity<?> createPastEvent(
            @RequestHeader("Authorization") String token,
            @PathVariable Long societyId,
            @RequestBody PastEventDTO eventDTO) {
        log.info("POST /api/society-dashboard/society/{}/events/past - Create past event", societyId);
        
        try {
            validateSocietyAccess(token, societyId);
            eventDTO.setSocietyId(societyId);
            PastEventDTO created = pastEventService.createPastEvent(eventDTO);
            return new ResponseEntity<>(created, HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("Failed to create past event", e);
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    @PutMapping("/society/{societyId}/events/past/{eventId}")
    public ResponseEntity<?> updatePastEvent(
            @RequestHeader("Authorization") String token,
            @PathVariable Long societyId,
            @PathVariable Long eventId,
            @RequestBody PastEventDTO eventDTO) {
        log.info("PUT /api/society-dashboard/society/{}/events/past/{} - Update past event", societyId, eventId);
        
        try {
            validateSocietyAccess(token, societyId);
            eventDTO.setId(eventId);
            eventDTO.setSocietyId(societyId);
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

    @DeleteMapping("/society/{societyId}/events/past/{eventId}")
    public ResponseEntity<?> deletePastEvent(
            @RequestHeader("Authorization") String token,
            @PathVariable Long societyId,
            @PathVariable Long eventId) {
        log.info("DELETE /api/society-dashboard/society/{}/events/past/{} - Delete past event", societyId, eventId);
        
        try {
            validateSocietyAccess(token, societyId);
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

    @GetMapping("/society/{societyId}/events/upcoming")
    public ResponseEntity<?> getSocietyUpcomingEvents(
            @RequestHeader("Authorization") String token,
            @PathVariable Long societyId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        log.info("GET /api/society-dashboard/society/{}/events/upcoming - Get society upcoming events", societyId);
        
        try {
            validateSocietyAccess(token, societyId);
            List<UpcomingEventDTO> events = upcomingEventService.getEventsBySocietyId(societyId);
            return ResponseEntity.ok(events);
        } catch (Exception e) {
            log.error("Failed to get society upcoming events", e);
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
        }
    }

    @PostMapping("/society/{societyId}/events/upcoming")
    public ResponseEntity<?> createUpcomingEvent(
            @RequestHeader("Authorization") String token,
            @PathVariable Long societyId,
            @RequestBody UpcomingEventDTO eventDTO) {
        log.info("POST /api/society-dashboard/society/{}/events/upcoming - Create upcoming event", societyId);
        
        try {
            validateSocietyAccess(token, societyId);
            eventDTO.setSocietyId(societyId);
            UpcomingEventDTO created = upcomingEventService.createUpcomingEvent(eventDTO);
            return new ResponseEntity<>(created, HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("Failed to create upcoming event", e);
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    @PutMapping("/society/{societyId}/events/upcoming/{eventId}")
    public ResponseEntity<?> updateUpcomingEvent(
            @RequestHeader("Authorization") String token,
            @PathVariable Long societyId,
            @PathVariable Long eventId,
            @RequestBody UpcomingEventDTO eventDTO) {
        log.info("PUT /api/society-dashboard/society/{}/events/upcoming/{} - Update upcoming event", societyId, eventId);
        
        try {
            validateSocietyAccess(token, societyId);
            eventDTO.setId(eventId);
            eventDTO.setSocietyId(societyId);
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

    @DeleteMapping("/society/{societyId}/events/upcoming/{eventId}")
    public ResponseEntity<?> deleteUpcomingEvent(
            @RequestHeader("Authorization") String token,
            @PathVariable Long societyId,
            @PathVariable Long eventId) {
        log.info("DELETE /api/society-dashboard/society/{}/events/upcoming/{} - Delete upcoming event", societyId, eventId);
        
        try {
            validateSocietyAccess(token, societyId);
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

    @GetMapping("/society/{societyId}/achievements")
    public ResponseEntity<?> getSocietyAchievements(
            @RequestHeader("Authorization") String token,
            @PathVariable Long societyId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        log.info("GET /api/society-dashboard/society/{}/achievements - Get society achievements", societyId);
        
        try {
            validateSocietyAccess(token, societyId);
            List<AchievementDTO> achievements = achievementService.getAchievementsBySocietyId(societyId);
            return ResponseEntity.ok(achievements);
        } catch (Exception e) {
            log.error("Failed to get society achievements", e);
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
        }
    }

    @PostMapping("/society/{societyId}/achievements")
    public ResponseEntity<?> createAchievement(
            @RequestHeader("Authorization") String token,
            @PathVariable Long societyId,
            @RequestBody AchievementDTO achievementDTO) {
        log.info("POST /api/society-dashboard/society/{}/achievements - Create achievement", societyId);
        
        try {
            validateSocietyAccess(token, societyId);
            achievementDTO.setSocietyId(societyId);
            AchievementDTO created = achievementService.createAchievement(achievementDTO);
            return new ResponseEntity<>(created, HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("Failed to create achievement", e);
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    @PutMapping("/society/{societyId}/achievements/{achievementId}")
    public ResponseEntity<?> updateAchievement(
            @RequestHeader("Authorization") String token,
            @PathVariable Long societyId,
            @PathVariable Long achievementId,
            @RequestBody AchievementDTO achievementDTO) {
        log.info("PUT /api/society-dashboard/society/{}/achievements/{} - Update achievement", societyId, achievementId);
        
        try {
            validateSocietyAccess(token, societyId);
            achievementDTO.setId(achievementId);
            achievementDTO.setSocietyId(societyId);
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

    @DeleteMapping("/society/{societyId}/achievements/{achievementId}")
    public ResponseEntity<?> deleteAchievement(
            @RequestHeader("Authorization") String token,
            @PathVariable Long societyId,
            @PathVariable Long achievementId) {
        log.info("DELETE /api/society-dashboard/society/{}/achievements/{} - Delete achievement", societyId, achievementId);
        
        try {
            validateSocietyAccess(token, societyId);
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

    @GetMapping("/society/{societyId}/gallery")
    public ResponseEntity<?> getSocietyGalleryItems(
            @RequestHeader("Authorization") String token,
            @PathVariable Long societyId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        log.info("GET /api/society-dashboard/society/{}/gallery - Get society gallery items", societyId);
        
        try {
            validateSocietyAccess(token, societyId);
            List<GalleryItemDTO> items = galleryItemService.getItemsBySocietyId(societyId);
            return ResponseEntity.ok(items);
        } catch (Exception e) {
            log.error("Failed to get society gallery items", e);
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
        }
    }

    @PostMapping("/society/{societyId}/gallery")
    public ResponseEntity<?> createGalleryItem(
            @RequestHeader("Authorization") String token,
            @PathVariable Long societyId,
            @RequestBody GalleryItemDTO itemDTO) {
        log.info("POST /api/society-dashboard/society/{}/gallery - Create gallery item", societyId);
        
        try {
            validateSocietyAccess(token, societyId);
            itemDTO.setSocietyId(societyId);
            GalleryItemDTO created = galleryItemService.createGalleryItem(itemDTO);
            return new ResponseEntity<>(created, HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("Failed to create gallery item", e);
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    @DeleteMapping("/society/{societyId}/gallery/{itemId}")
    public ResponseEntity<?> deleteGalleryItem(
            @RequestHeader("Authorization") String token,
            @PathVariable Long societyId,
            @PathVariable Long itemId) {
        log.info("DELETE /api/society-dashboard/society/{}/gallery/{} - Delete gallery item", societyId, itemId);
        
        try {
            validateSocietyAccess(token, societyId);
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
