package com.example.demo.repository;

import com.example.demo.model.Notification;
import com.example.demo.model.Society;
import com.example.demo.model.UpcomingEvent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    
    List<Notification> findAllByOrderByTimeDesc();
    
    Page<Notification> findAllByOrderByTimeDesc(Pageable pageable);
    
    List<Notification> findByUnreadTrueOrderByTimeDesc();
    
    List<Notification> findByUnreadFalseOrderByTimeDesc();
    
    List<Notification> findByType(Notification.NotificationType type);
    
    List<Notification> findByTypeOrderByTimeDesc(Notification.NotificationType type);
    
    List<Notification> findByPriority(Notification.PriorityLevel priority);
    
    List<Notification> findBySociety(Society society);
    
    List<Notification> findByRelatedEvent(UpcomingEvent relatedEvent);
    
    List<Notification> findByTargetAudience(String targetAudience);
    
    @Query("SELECT n FROM Notification n WHERE n.expiryDate IS NULL OR n.expiryDate > :currentDate ORDER BY n.time DESC")
    List<Notification> findActiveNotifications(@Param("currentDate") LocalDateTime currentDate);
    
    @Query("SELECT n FROM Notification n WHERE n.expiryDate <= :currentDate")
    List<Notification> findExpiredNotifications(@Param("currentDate") LocalDateTime currentDate);
    
    @Query("SELECT n FROM Notification n WHERE n.time BETWEEN :startDate AND :endDate ORDER BY n.time DESC")
    List<Notification> findNotificationsByDateRange(@Param("startDate") LocalDateTime startDate, 
                                                    @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT n FROM Notification n WHERE LOWER(n.title) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "OR LOWER(n.message) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Notification> searchNotificationsByKeyword(@Param("keyword") String keyword);
    
    @Query("SELECT n FROM Notification n WHERE n.priority IN :priorities ORDER BY n.time DESC")
    List<Notification> findNotificationsByPriorities(@Param("priorities") List<Notification.PriorityLevel> priorities);
    
    @Query("SELECT n.type, COUNT(n) FROM Notification n GROUP BY n.type ORDER BY COUNT(n) DESC")
    List<Object[]> getNotificationCountByType();
    
    long countByUnreadTrue();
    
    long countByType(Notification.NotificationType type);
    
    long countByPriority(Notification.PriorityLevel priority);
}
