package com.example.demo.controller;

import com.example.demo.dto.NotificationDTO;
import com.example.demo.model.Notification;
import com.example.demo.service.NotificationService;
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
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping
    public ResponseEntity<List<NotificationDTO>> getAllNotifications() {
        log.info("GET /api/notifications - Fetching all notifications");
        List<NotificationDTO> notifications = notificationService.getAllNotifications();
        return ResponseEntity.ok(notifications);
    }

    @GetMapping("/paged")
    public ResponseEntity<Page<NotificationDTO>> getAllNotifications(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        log.info("GET /api/notifications/paged - Fetching notifications with pagination");
        Pageable pageable = PageRequest.of(page, size);
        Page<NotificationDTO> notifications = notificationService.getAllNotifications(pageable);
        return ResponseEntity.ok(notifications);
    }

    @GetMapping("/{id}")
    public ResponseEntity<NotificationDTO> getNotificationById(@PathVariable Long id) {
        log.info("GET /api/notifications/{} - Fetching notification by id", id);
        return notificationService.getNotificationById(id)
                .map(notification -> ResponseEntity.ok(notification))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/unread")
    public ResponseEntity<List<NotificationDTO>> getUnreadNotifications() {
        log.info("GET /api/notifications/unread - Fetching unread notifications");
        List<NotificationDTO> notifications = notificationService.getUnreadNotifications();
        return ResponseEntity.ok(notifications);
    }

    @GetMapping("/active")
    public ResponseEntity<List<NotificationDTO>> getActiveNotifications() {
        log.info("GET /api/notifications/active - Fetching active notifications");
        List<NotificationDTO> notifications = notificationService.getActiveNotifications();
        return ResponseEntity.ok(notifications);
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<NotificationDTO>> getNotificationsByType(@PathVariable Notification.NotificationType type) {
        log.info("GET /api/notifications/type/{} - Fetching notifications by type", type);
        List<NotificationDTO> notifications = notificationService.getNotificationsByType(type);
        return ResponseEntity.ok(notifications);
    }

    @GetMapping("/priority/{priority}")
    public ResponseEntity<List<NotificationDTO>> getNotificationsByPriority(@PathVariable Notification.PriorityLevel priority) {
        log.info("GET /api/notifications/priority/{} - Fetching notifications by priority", priority);
        List<NotificationDTO> notifications = notificationService.getNotificationsByPriority(priority);
        return ResponseEntity.ok(notifications);
    }

    @GetMapping("/search")
    public ResponseEntity<List<NotificationDTO>> searchNotifications(@RequestParam String keyword) {
        log.info("GET /api/notifications/search?keyword={} - Searching notifications", keyword);
        List<NotificationDTO> notifications = notificationService.searchNotifications(keyword);
        return ResponseEntity.ok(notifications);
    }

    @GetMapping("/count/unread")
    public ResponseEntity<Long> getUnreadNotificationsCount() {
        log.info("GET /api/notifications/count/unread - Fetching unread notifications count");
        long count = notificationService.getUnreadNotificationsCount();
        return ResponseEntity.ok(count);
    }

    @GetMapping("/statistics/by-type")
    public ResponseEntity<List<Object[]>> getNotificationCountByType() {
        log.info("GET /api/notifications/statistics/by-type - Fetching notification count by type");
        List<Object[]> statistics = notificationService.getNotificationCountByType();
        return ResponseEntity.ok(statistics);
    }

    @PostMapping
    public ResponseEntity<NotificationDTO> createNotification(@RequestBody NotificationDTO notificationDTO) {
        log.info("POST /api/notifications - Creating new notification: {}", notificationDTO.getTitle());
        NotificationDTO createdNotification = notificationService.createNotification(notificationDTO);
        return new ResponseEntity<>(createdNotification, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<NotificationDTO> updateNotification(@PathVariable Long id, @RequestBody NotificationDTO notificationDTO) {
        log.info("PUT /api/notifications/{} - Updating notification", id);
        return notificationService.updateNotification(id, notificationDTO)
                .map(updatedNotification -> ResponseEntity.ok(updatedNotification))
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}/mark-read")
    public ResponseEntity<Void> markAsRead(@PathVariable Long id) {
        log.info("PATCH /api/notifications/{}/mark-read - Marking notification as read", id);
        boolean marked = notificationService.markAsRead(id);
        return marked ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    @PatchMapping("/{id}/mark-unread")
    public ResponseEntity<Void> markAsUnread(@PathVariable Long id) {
        log.info("PATCH /api/notifications/{}/mark-unread - Marking notification as unread", id);
        boolean marked = notificationService.markAsUnread(id);
        return marked ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNotification(@PathVariable Long id) {
        log.info("DELETE /api/notifications/{} - Deleting notification", id);
        boolean deleted = notificationService.deleteNotification(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
