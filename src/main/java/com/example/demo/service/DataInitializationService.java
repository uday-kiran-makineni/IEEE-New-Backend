package com.example.demo.service;

import com.example.demo.model.*;
import com.example.demo.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class DataInitializationService {  // Removed CommandLineRunner to disable automatic initialization

    private final SocietyRepository societyRepository;
    private final CouncilRepository councilRepository;
    private final PastEventRepository pastEventRepository;
    private final UpcomingEventRepository upcomingEventRepository;
    private final AchievementRepository achievementRepository;
    private final GalleryItemRepository galleryItemRepository;
    private final NotificationRepository notificationRepository;
    private final HeroSlideRepository heroSlideRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    // Commented out automatic initialization - use database-seeder.html instead
    /*
    @Override
    @Transactional
    public void run(String... args) throws Exception {
        log.info("Starting data initialization...");
        
        // Always check and initialize users
        initializeUsers();
        
        // Only initialize other data if database is empty
        if (societyRepository.count() == 0) {
            initializeSocieties();
            initializeCouncils();
            initializeHeroSlides();
            initializeAchievements();
            initializePastEvents();
            initializeUpcomingEvents();
            initializeNotifications();
            initializeGalleryItems();
            
            log.info("Data initialization completed successfully!");
        } else {
            log.info("Database already contains data. Skipping initialization.");
        }
    }
    */

    private void initializeSocieties() {
        log.info("Initializing societies...");
        
        List<Society> societies = Arrays.asList(
            createSociety("IEEE HKN Society", "https://cdn-icons-png.flaticon.com/512/1995/1995507.png", "Honor society for electrical and computer engineers", 2020),
            createSociety("IEEE Circuits and Systems Society", "https://cdn-icons-png.flaticon.com/512/2721/2721297.png", "Focuses on circuits and systems engineering", 2019),
            createSociety("IEEE Signal Processing Society", "https://cdn-icons-png.flaticon.com/512/2921/2921222.png", "Dedicated to signal processing technologies", 2018),
            createSociety("IEEE Communication Society", "https://cdn-icons-png.flaticon.com/512/3062/3062634.png", "Advancing communication technologies", 2017),
            createSociety("IEEE Computer Society", "https://cdn-icons-png.flaticon.com/512/2721/2721297.png", "Leading society for computing professionals", 2016),
            createSociety("IEEE Robotics and Automation Society", "https://cdn-icons-png.flaticon.com/512/4712/4712035.png", "Robotics and automation technologies", 2021)
        );
        
        societyRepository.saveAll(societies);
        log.info("Societies initialized: {}", societies.size());
    }

    private void initializeCouncils() {
        log.info("Initializing councils...");
        
        List<Council> councils = Arrays.asList(
            createCouncil("IEEE Council on Electronic Design Automation", "https://cdn-icons-png.flaticon.com/512/2721/2721297.png", "Electronic design automation advancement", 2018),
            createCouncil("IEEE Council on RFID", "https://cdn-icons-png.flaticon.com/512/3135/3135755.png", "Radio frequency identification technologies", 2019),
            createCouncil("IEEE Systems Council", "https://cdn-icons-png.flaticon.com/512/4712/4712035.png", "Systems engineering and integration", 2017),
            createCouncil("IEEE Nanotechnology Council", "https://cdn-icons-png.flaticon.com/512/2721/2721297.png", "Nanotechnology research and development", 2020)
        );
        
        councilRepository.saveAll(councils);
        log.info("Councils initialized: {}", councils.size());
    }

    private void initializeHeroSlides() {
        log.info("Initializing hero slides...");
        
        List<HeroSlide> heroSlides = Arrays.asList(
            createHeroSlide("Welcome to IEEE Vardhaman", "Advancing Technology for Humanity", 
                "https://images.pexels.com/photos/1181673/pexels-photo-1181673.jpeg?auto=compress&cs=tinysrgb&w=1920&h=1080&fit=crop", 
                "Join the world's largest technical professional organization", 1),
            createHeroSlide("Innovation & Excellence", "Shaping the Future of Technology", 
                "https://images.pexels.com/photos/2152/sky-earth-space-working.jpg?auto=compress&cs=tinysrgb&w=1920&h=1080&fit=crop", 
                "Empowering students through technical excellence and innovation", 2),
            createHeroSlide("Technical Leadership", "Building Tomorrow's Engineers", 
                "https://images.pexels.com/photos/3861969/pexels-photo-3861969.jpeg?auto=compress&cs=tinysrgb&w=1920&h=1080&fit=crop", 
                "Fostering leadership and professional development", 3)
        );
        
        heroSlideRepository.saveAll(heroSlides);
        log.info("Hero slides initialized: {}", heroSlides.size());
    }

    private void initializeAchievements() {
        log.info("Initializing achievements...");
        
        List<Achievement> achievements = Arrays.asList(
            createAchievement("Best Student Branch Award", "2024", "IEEE Region 10 Recognition", "https://res.cloudinary.com/doyh3fqr5/image/upload/v1750567600/download_aib5cd.jpg", true),
            createAchievement("Outstanding Volunteer Award", "2023", "Individual Excellence Recognition", "https://res.cloudinary.com/doyh3fqr5/image/upload/v1750567579/download_eldpyw.png", true),
            createAchievement("Technical Innovation Prize", "2023", "IEEE India Council Award", "https://res.cloudinary.com/doyh3fqr5/image/upload/v1750567570/download_lxk9gv.jpg", true)
        );
        
        achievementRepository.saveAll(achievements);
        log.info("Achievements initialized: {}", achievements.size());
    }

    private void initializePastEvents() {
        log.info("Initializing past events...");
        
        List<PastEvent> pastEvents = Arrays.asList(
            createPastEvent("International Conference on AI", "March 15, 2024", 
                "https://images.pexels.com/photos/8386440/pexels-photo-8386440.jpeg?auto=compress&cs=tinysrgb&w=800", 
                "500+", "IEEE Computer Society", 4.5),
            createPastEvent("Robotics Workshop", "February 20, 2024", 
                "https://images.pexels.com/photos/8386434/pexels-photo-8386434.jpeg?auto=compress&cs=tinysrgb&w=800", 
                "200+", "IEEE Robotics & Automation Society", 4.8),
            createPastEvent("IEEE Day Celebration", "October 1, 2023", 
                "https://images.pexels.com/photos/1181406/pexels-photo-1181406.jpeg?auto=compress&cs=tinysrgb&w=800", 
                "1000+", "IEEE Student Branch", 4.7)
        );
        
        pastEventRepository.saveAll(pastEvents);
        log.info("Past events initialized: {}", pastEvents.size());
    }

    private void initializeUpcomingEvents() {
        log.info("Initializing upcoming events...");
        
        List<UpcomingEvent> upcomingEvents = Arrays.asList(
            createUpcomingEvent("5G Technology Symposium", "June 15, 2025", 
                "https://images.pexels.com/photos/8386440/pexels-photo-8386440.jpeg?auto=compress&cs=tinysrgb&w=800", 
                "Exploring the future of wireless communication", "150", "IEEE Communications Society"),
            createUpcomingEvent("Machine Learning Bootcamp", "July 10, 2025", 
                "https://images.pexels.com/photos/8386434/pexels-photo-8386434.jpeg?auto=compress&cs=tinysrgb&w=800", 
                "Hands-on ML training for students", "250", "IEEE Computer Society"),
            createUpcomingEvent("Entrepreneurship Summit", "August 5, 2025", 
                "https://images.pexels.com/photos/1181406/pexels-photo-1181406.jpeg?auto=compress&cs=tinysrgb&w=800", 
                "Innovation and startup ecosystem", "100", "IEEE Young Professionals")
        );
        
        upcomingEventRepository.saveAll(upcomingEvents);
        log.info("Upcoming events initialized: {}", upcomingEvents.size());
    }

    private void initializeNotifications() {
        log.info("Initializing notifications...");
        
        List<Notification> notifications = Arrays.asList(
            createNotification("New Event Registration Open", "5G Technology Symposium registration is now open", 
                Notification.NotificationType.EVENT, Notification.PriorityLevel.HIGH),
            createNotification("Achievement Unlocked", "IEEE Vardhaman wins Best Student Branch Award 2024", 
                Notification.NotificationType.ACHIEVEMENT, Notification.PriorityLevel.HIGH),
            createNotification("Workshop Reminder", "Machine Learning Bootcamp starts tomorrow", 
                Notification.NotificationType.REMINDER, Notification.PriorityLevel.MEDIUM),
            createNotification("New Society Member", "Welcome to Computer Society! Check your email for details", 
                Notification.NotificationType.MEMBERSHIP, Notification.PriorityLevel.LOW)
        );
        
        notificationRepository.saveAll(notifications);
        log.info("Notifications initialized: {}", notifications.size());
    }

    private void initializeGalleryItems() {
        log.info("Initializing gallery items...");
        
        List<GalleryItem> galleryItems = Arrays.asList(
            createGalleryItem("https://images.pexels.com/photos/1763075/pexels-photo-1763075.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1", 300, "Workshop", "Technical Workshop Session"),
            createGalleryItem("https://images.pexels.com/photos/256490/pexels-photo-256490.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1", 250, "Conference", "IEEE Conference 2024"),
            createGalleryItem("https://images.pexels.com/photos/1000653/pexels-photo-1000653.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1", 350, "Event", "Student Activities"),
            createGalleryItem("https://images.pexels.com/photos/3401403/pexels-photo-3401403.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1", 280, "Achievement", "Award Ceremony"),
            createGalleryItem("https://images.pexels.com/photos/3771089/pexels-photo-3771089.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1", 320, "Team", "IEEE Team Members")
        );
        
        galleryItemRepository.saveAll(galleryItems);
        log.info("Gallery items initialized: {}", galleryItems.size());
    }

    // Helper methods
    private Society createSociety(String name, String image, String description, int establishedYear) {
        Society society = new Society();
        society.setName(name);
        society.setImage(image);
        society.setDescription(description);
        society.setIsActive(true);
        society.setMemberCount((int) (Math.random() * 100) + 50);
        society.setEstablishedYear(establishedYear);
        return society;
    }

    private Council createCouncil(String name, String image, String description, int establishedYear) {
        Council council = new Council();
        council.setName(name);
        council.setImage(image);
        council.setDescription(description);
        council.setIsActive(true);
        council.setEstablishedYear(establishedYear);
        return council;
    }

    private HeroSlide createHeroSlide(String title, String subtitle, String image, String description, int displayOrder) {
        HeroSlide heroSlide = new HeroSlide();
        heroSlide.setTitle(title);
        heroSlide.setSubtitle(subtitle);
        heroSlide.setImage(image);
        heroSlide.setDescription(description);
        heroSlide.setDisplayOrder(displayOrder);
        heroSlide.setIsActive(true);
        return heroSlide;
    }

    private Achievement createAchievement(String title, String year, String description, String image, boolean isFeatured) {
        Achievement achievement = new Achievement();
        achievement.setTitle(title);
        achievement.setYear(year);
        achievement.setDescription(description);
        achievement.setImage(image);
        achievement.setIsFeatured(isFeatured);
        achievement.setAchievementDate(LocalDateTime.now().minusDays((int) (Math.random() * 365)));
        return achievement;
    }

    private PastEvent createPastEvent(String title, String dateStr, String image, String participants, String hostingBranch, double rating) {
        PastEvent pastEvent = new PastEvent();
        pastEvent.setTitle(title);
        pastEvent.setEventDate(parseDate(dateStr));
        pastEvent.setImage(image);
        pastEvent.setParticipants(participants);
        pastEvent.setHostingBranchName(hostingBranch);
        pastEvent.setFeedbackRating(rating);
        return pastEvent;
    }

    private UpcomingEvent createUpcomingEvent(String title, String dateStr, String image, String description, String registrations, String hostingBranch) {
        UpcomingEvent upcomingEvent = new UpcomingEvent();
        upcomingEvent.setTitle(title);
        upcomingEvent.setEventDate(parseDate(dateStr));
        upcomingEvent.setImage(image);
        upcomingEvent.setDescription(description);
        upcomingEvent.setRegistrations(registrations);
        upcomingEvent.setHostingBranchName(hostingBranch);
        upcomingEvent.setIsRegistrationOpen(true);
        upcomingEvent.setRegistrationFee(0.0);
        upcomingEvent.setEventType(UpcomingEvent.EventType.WORKSHOP);
        return upcomingEvent;
    }

    private Notification createNotification(String title, String message, Notification.NotificationType type, Notification.PriorityLevel priority) {
        Notification notification = new Notification();
        notification.setTitle(title);
        notification.setMessage(message);
        notification.setTime(LocalDateTime.now().minusHours((int) (Math.random() * 72)));
        notification.setType(type);
        notification.setPriority(priority);
        notification.setUnread(Math.random() > 0.3); // 70% chance of being unread
        notification.setTargetAudience("ALL");
        return notification;
    }

    private GalleryItem createGalleryItem(String imageUrl, int height, String category, String title) {
        GalleryItem galleryItem = new GalleryItem();
        galleryItem.setImg(imageUrl);
        galleryItem.setUrl("#");
        galleryItem.setHeight(height);
        galleryItem.setTitle(title);
        galleryItem.setCategory(category);
        galleryItem.setIsFeatured(Math.random() > 0.7); // 30% chance of being featured
        galleryItem.setUploadDate(LocalDateTime.now().minusDays((int) (Math.random() * 30)));
        return galleryItem;
    }

    private LocalDateTime parseDate(String dateStr) {
        try {
            return LocalDateTime.parse(dateStr + " 10:00", DateTimeFormatter.ofPattern("MMMM d, yyyy H:mm"));
        } catch (Exception e) {
            log.warn("Could not parse date: {}, using current time", dateStr);
            return LocalDateTime.now();
        }
    }

    private void initializeUsers() {
        log.info("=== INITIALIZING TEST USERS ===");
        
        // Always log existing users for debugging
        List<User> existingUsers = userRepository.findAll();
        log.info("Current users in database: {}", existingUsers.size());
        for (User user : existingUsers) {
            log.info("Existing user: email={}, role={}, entityId={}", 
                user.getEmail(), user.getRole(), user.getEntityId());
        }
        
        // Update existing users that have null entityId
        boolean updatedUsers = false;
        for (User user : existingUsers) {
            if (user.getEntityId() == null) {
                if (user.getRole() == User.UserRole.SOCIETY_ADMIN) {
                    user.setEntityId(1L);
                    userRepository.save(user);
                    log.info("Updated {} with entityId=1 (society)", user.getEmail());
                    updatedUsers = true;
                } else if (user.getRole() == User.UserRole.COUNCIL_ADMIN) {
                    user.setEntityId(1L);
                    userRepository.save(user);
                    log.info("Updated {} with entityId=1 (council)", user.getEmail());
                    updatedUsers = true;
                }
            }
        }
        
        if (updatedUsers) {
            log.info("Updated existing users with proper entityId values");
            // Re-fetch to show updated state
            existingUsers = userRepository.findAll();
            log.info("Updated users in database:");
            for (User user : existingUsers) {
                log.info("Updated user: email={}, role={}, entityId={}", 
                    user.getEmail(), user.getRole(), user.getEntityId());
            }
        }
        
        // Check if our specific test users exist (including missing demo users from frontend)
        boolean hknUserExists = userRepository.existsByEmail("hkn@ieee.vardhaman.edu");
        boolean casUserExists = userRepository.existsByEmail("cas@ieee.vardhaman.edu");
        boolean cedaUserExists = userRepository.existsByEmail("ceda@ieee.vardhaman.edu");
        boolean rfidUserExists = userRepository.existsByEmail("rfid@ieee.vardhaman.edu");
        boolean societyUserExists = userRepository.existsByEmail("society@ieee.vardhaman.edu");
        boolean councilUserExists = userRepository.existsByEmail("council@ieee.vardhaman.edu");
        
        if (!hknUserExists || !casUserExists || !cedaUserExists || !rfidUserExists || !societyUserExists || !councilUserExists) {
            log.info("Creating missing test users...");
            try {
                // Create Society Admin - assign to first society
                User societyAdmin = new User();
                societyAdmin.setEmail("society@ieee.vardhaman.edu");
                societyAdmin.setFullName("Society Admin");
                societyAdmin.setPassword("society123");
                societyAdmin.setRole(User.UserRole.SOCIETY_ADMIN);
                societyAdmin.setIsActive(true);
                societyAdmin.setEmailVerified(true);
                societyAdmin.setEntityId(1L); // Assign to society ID 1
                if (!societyUserExists) {
                    userService.createUser(societyAdmin);
                    log.info("Created Society Admin user");
                }
                
                // Create Council Admin - assign to first council
                User councilAdmin = new User();
                councilAdmin.setEmail("council@ieee.vardhaman.edu");
                councilAdmin.setFullName("Council Admin");
                councilAdmin.setPassword("council123");
                councilAdmin.setRole(User.UserRole.COUNCIL_ADMIN);
                councilAdmin.setIsActive(true);
                councilAdmin.setEmailVerified(true);
                councilAdmin.setEntityId(1L); // Assign to council ID 1
                if (!councilUserExists) {
                    userService.createUser(councilAdmin);
                    log.info("Created Council Admin user");
                }
                
                // Create HKN Society Admin - assign to HKN society (ID 1)
                User hknAdmin = new User();
                hknAdmin.setEmail("hkn@ieee.vardhaman.edu");
                hknAdmin.setFullName("HKN Society Admin");
                hknAdmin.setPassword("hkn123");
                hknAdmin.setRole(User.UserRole.SOCIETY_ADMIN);
                hknAdmin.setIsActive(true);
                hknAdmin.setEmailVerified(true);
                hknAdmin.setEntityId(1L); // Assign to society ID 1 (IEEE HKN Society)
                if (!hknUserExists) {
                    userService.createUser(hknAdmin);
                    log.info("Created HKN Society Admin user");
                }
                
                // Create CAS Society Admin - assign to CAS society (ID 2)
                User casAdmin = new User();
                casAdmin.setEmail("cas@ieee.vardhaman.edu");
                casAdmin.setFullName("CAS Society Admin");
                casAdmin.setPassword("cas123");
                casAdmin.setRole(User.UserRole.SOCIETY_ADMIN);
                casAdmin.setIsActive(true);
                casAdmin.setEmailVerified(true);
                casAdmin.setEntityId(2L); // Assign to society ID 2 (IEEE Circuits and Systems Society)
                if (!casUserExists) {
                    userService.createUser(casAdmin);
                    log.info("Created CAS Society Admin user");
                }
                
                // Create CEDA Council Admin - assign to CEDA council (ID 1)
                User cedaAdmin = new User();
                cedaAdmin.setEmail("ceda@ieee.vardhaman.edu");
                cedaAdmin.setFullName("CEDA Council Admin");
                cedaAdmin.setPassword("ceda123");
                cedaAdmin.setRole(User.UserRole.COUNCIL_ADMIN);
                cedaAdmin.setIsActive(true);
                cedaAdmin.setEmailVerified(true);
                cedaAdmin.setEntityId(1L); // Assign to council ID 1 (IEEE Council on Electronic Design Automation)
                if (!cedaUserExists) {
                    userService.createUser(cedaAdmin);
                    log.info("Created CEDA Council Admin user");
                }
                
                // Create RFID Council Admin - assign to RFID council (ID 2)
                User rfidAdmin = new User();
                rfidAdmin.setEmail("rfid@ieee.vardhaman.edu");
                rfidAdmin.setFullName("RFID Council Admin");
                rfidAdmin.setPassword("rfid123");
                rfidAdmin.setRole(User.UserRole.COUNCIL_ADMIN);
                rfidAdmin.setIsActive(true);
                rfidAdmin.setEmailVerified(true);
                rfidAdmin.setEntityId(2L); // Assign to council ID 2 (IEEE Council on RFID)
                if (!rfidUserExists) {
                    userService.createUser(rfidAdmin);
                    log.info("Created RFID Council Admin user");
                }
                
                log.info("Test users initialization completed");
            } catch (Exception e) {
                log.error("Error initializing test users", e);
            }
        } else if (!updatedUsers) {
            log.info("All test users already exist with proper entityId, skipping user initialization");
        }
    }
}
