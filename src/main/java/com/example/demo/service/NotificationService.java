package com.example.demo.service;

import com.example.demo.dto.NotificationDTO;
import com.example.demo.model.Notification;
import com.example.demo.model.Society;
import com.example.demo.model.UpcomingEvent;
import com.example.demo.repository.NotificationRepository;
import com.example.demo.repository.SocietyRepository;
import com.example.demo.repository.UpcomingEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final SocietyRepository societyRepository;
    private final UpcomingEventRepository upcomingEventRepository;

    public List<NotificationDTO> getAllNotifications() {
        log.info("Fetching all notifications");
        return notificationRepository.findAllByOrderByTimeDesc()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Page<NotificationDTO> getAllNotifications(Pageable pageable) {
        log.info("Fetching notifications with pagination");
        return notificationRepository.findAllByOrderByTimeDesc(pageable)
                .map(this::convertToDTO);
    }

    public List<NotificationDTO> getUnreadNotifications() {
        log.info("Fetching unread notifications");
        return notificationRepository.findByUnreadTrueOrderByTimeDesc()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<NotificationDTO> getActiveNotifications() {
        log.info("Fetching active notifications");
        return notificationRepository.findActiveNotifications(LocalDateTime.now())
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<NotificationDTO> getNotificationsByType(Notification.NotificationType type) {
        log.info("Fetching notifications by type: {}", type);
        return notificationRepository.findByTypeOrderByTimeDesc(type)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<NotificationDTO> getNotificationsByPriority(Notification.PriorityLevel priority) {
        log.info("Fetching notifications by priority: {}", priority);
        return notificationRepository.findByPriority(priority)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Optional<NotificationDTO> getNotificationById(Long id) {
        log.info("Fetching notification with id: {}", id);
        return notificationRepository.findById(id)
                .map(this::convertToDTO);
    }

    public NotificationDTO createNotification(NotificationDTO notificationDTO) {
        log.info("Creating new notification: {}", notificationDTO.getTitle());
        Notification notification = convertToEntity(notificationDTO);
        Notification savedNotification = notificationRepository.save(notification);
        return convertToDTO(savedNotification);
    }

    public Optional<NotificationDTO> updateNotification(Long id, NotificationDTO notificationDTO) {
        log.info("Updating notification with id: {}", id);
        return notificationRepository.findById(id)
                .map(existingNotification -> {
                    updateNotificationFields(existingNotification, notificationDTO);
                    Notification updatedNotification = notificationRepository.save(existingNotification);
                    return convertToDTO(updatedNotification);
                });
    }

    public boolean markAsRead(Long id) {
        log.info("Marking notification as read: {}", id);
        return notificationRepository.findById(id)
                .map(notification -> {
                    notification.setUnread(false);
                    notificationRepository.save(notification);
                    return true;
                })
                .orElse(false);
    }

    public boolean markAsUnread(Long id) {
        log.info("Marking notification as unread: {}", id);
        return notificationRepository.findById(id)
                .map(notification -> {
                    notification.setUnread(true);
                    notificationRepository.save(notification);
                    return true;
                })
                .orElse(false);
    }

    public boolean deleteNotification(Long id) {
        log.info("Deleting notification with id: {}", id);
        if (notificationRepository.existsById(id)) {
            notificationRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<NotificationDTO> searchNotifications(String keyword) {
        log.info("Searching notifications with keyword: {}", keyword);
        return notificationRepository.searchNotificationsByKeyword(keyword)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public long getUnreadNotificationsCount() {
        return notificationRepository.countByUnreadTrue();
    }

    public List<Object[]> getNotificationCountByType() {
        return notificationRepository.getNotificationCountByType();
    }

    private NotificationDTO convertToDTO(Notification notification) {
        return new NotificationDTO(
                notification.getId(),
                notification.getTitle(),
                notification.getMessage(),
                notification.getTime(),
                notification.getType(),
                notification.getUnread(),
                notification.getPriority(),
                notification.getExpiryDate(),
                notification.getTargetAudience(),
                notification.getRelatedEvent() != null ? notification.getRelatedEvent().getId() : null,
                notification.getRelatedEvent() != null ? notification.getRelatedEvent().getTitle() : null,
                notification.getSociety() != null ? notification.getSociety().getId() : null,
                notification.getSociety() != null ? notification.getSociety().getName() : null
        );
    }

    private Notification convertToEntity(NotificationDTO notificationDTO) {
        Notification notification = new Notification();
        updateNotificationFields(notification, notificationDTO);
        return notification;
    }

    private void updateNotificationFields(Notification notification, NotificationDTO notificationDTO) {
        notification.setTitle(notificationDTO.getTitle());
        notification.setMessage(notificationDTO.getMessage());
        notification.setTime(notificationDTO.getTime() != null ? notificationDTO.getTime() : LocalDateTime.now());
        notification.setType(notificationDTO.getType());
        notification.setUnread(notificationDTO.getUnread() != null ? notificationDTO.getUnread() : true);
        notification.setPriority(notificationDTO.getPriority() != null ? notificationDTO.getPriority() : Notification.PriorityLevel.MEDIUM);
        notification.setExpiryDate(notificationDTO.getExpiryDate());
        notification.setTargetAudience(notificationDTO.getTargetAudience());

        if (notificationDTO.getRelatedEventId() != null) {
            UpcomingEvent event = upcomingEventRepository.findById(notificationDTO.getRelatedEventId()).orElse(null);
            notification.setRelatedEvent(event);
        }

        if (notificationDTO.getSocietyId() != null) {
            Society society = societyRepository.findById(notificationDTO.getSocietyId()).orElse(null);
            notification.setSociety(society);
        }
    }
}
