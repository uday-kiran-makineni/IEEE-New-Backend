package com.example.demo.controller;

import com.example.demo.dto.*;
import com.example.demo.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class DashboardController {

    private final SocietyService societyService;
    private final CouncilService councilService;
    private final UpcomingEventService upcomingEventService;
    private final PastEventService pastEventService;
    private final AchievementService achievementService;
    private final NotificationService notificationService;
    private final GalleryItemService galleryItemService;
    private final HeroSlideService heroSlideService;

    @GetMapping("/home-data")
    public ResponseEntity<Map<String, Object>> getHomePageData() {
        log.info("GET /api/dashboard/home-data - Fetching home page data");
        
        Map<String, Object> homeData = new HashMap<>();
        
        try {
            // Get active hero slides
            List<HeroSlideDTO> heroSlides = heroSlideService.getActiveHeroSlides();
            homeData.put("heroSlides", heroSlides);
            
            // Get active societies (limit to first 12 for homepage)
            List<SocietyDTO> societies = societyService.getActiveSocieties();
            homeData.put("societies", societies.size() > 12 ? societies.subList(0, 12) : societies);
            
            // Get active councils (limit to first 8 for homepage)
            List<CouncilDTO> councils = councilService.getActiveCouncils();
            homeData.put("councils", councils.size() > 8 ? councils.subList(0, 8) : councils);
            
            // Get recent past events (limit to 6)
            List<PastEventDTO> pastEvents = pastEventService.getAllPastEvents();
            homeData.put("pastEvents", pastEvents.size() > 6 ? pastEvents.subList(0, 6) : pastEvents);
            
            // Get upcoming events with open registration (limit to 6)
            List<UpcomingEventDTO> upcomingEvents = upcomingEventService.getEventsWithOpenRegistration();
            homeData.put("upcomingEvents", upcomingEvents.size() > 6 ? upcomingEvents.subList(0, 6) : upcomingEvents);
            
            // Get featured achievements (limit to 4)
            List<AchievementDTO> achievements = achievementService.getFeaturedAchievements();
            if (achievements.isEmpty()) {
                achievements = achievementService.getAllAchievements();
            }
            homeData.put("achievements", achievements.size() > 4 ? achievements.subList(0, 4) : achievements);
            
            // Get recent gallery items (limit to 15)
            List<GalleryItemDTO> galleryItems = galleryItemService.getAllGalleryItems();
            homeData.put("galleryItems", galleryItems.size() > 15 ? galleryItems.subList(0, 15) : galleryItems);
            
            // Get active notifications (limit to 5)
            List<NotificationDTO> notifications = notificationService.getActiveNotifications();
            homeData.put("notifications", notifications.size() > 5 ? notifications.subList(0, 5) : notifications);
            
            log.info("Successfully fetched home page data");
            return ResponseEntity.ok(homeData);
            
        } catch (Exception e) {
            log.error("Error fetching home page data", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/statistics")
    public ResponseEntity<Map<String, Object>> getDashboardStatistics() {
        log.info("GET /api/dashboard/statistics - Fetching dashboard statistics");
        
        Map<String, Object> statistics = new HashMap<>();
        
        try {
            // Count statistics
            statistics.put("totalSocieties", societyService.getActiveSocietiesCount());
            statistics.put("totalCouncils", councilService.getActiveCouncilsCount());
            statistics.put("upcomingEventsCount", upcomingEventService.getUpcomingEventsCount());
            statistics.put("openRegistrationEventsCount", upcomingEventService.getOpenRegistrationEventsCount());
            statistics.put("featuredAchievementsCount", achievementService.getFeaturedAchievementsCount());
            statistics.put("unreadNotificationsCount", notificationService.getUnreadNotificationsCount());
            statistics.put("featuredGalleryItemsCount", galleryItemService.getFeaturedGalleryItemsCount());
            statistics.put("activeHeroSlidesCount", heroSlideService.getActiveHeroSlidesCount());
            
            // Time-based statistics
            statistics.put("currentYear", LocalDateTime.now().getYear());
            
            log.info("Successfully fetched dashboard statistics");
            return ResponseEntity.ok(statistics);
            
        } catch (Exception e) {
            log.error("Error fetching dashboard statistics", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/recent-activities")
    public ResponseEntity<Map<String, Object>> getRecentActivities() {
        log.info("GET /api/dashboard/recent-activities - Fetching recent activities");
        
        Map<String, Object> activities = new HashMap<>();
        
        try {
            // Recent notifications (last 10)
            List<NotificationDTO> recentNotifications = notificationService.getAllNotifications();
            activities.put("recentNotifications", 
                recentNotifications.size() > 10 ? recentNotifications.subList(0, 10) : recentNotifications);
            
            // Recent gallery uploads (last 8)
            List<GalleryItemDTO> recentGallery = galleryItemService.getAllGalleryItems();
            activities.put("recentGallery", 
                recentGallery.size() > 8 ? recentGallery.subList(0, 8) : recentGallery);
            
            log.info("Successfully fetched recent activities");
            return ResponseEntity.ok(activities);
            
        } catch (Exception e) {
            log.error("Error fetching recent activities", e);
            return ResponseEntity.internalServerError().build();
        }
    }
}
