package com.example.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Notification {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 500)
    private String title;
    
    @Column(columnDefinition = "TEXT")
    private String message;
    
    @Column(name = "notification_time")
    private LocalDateTime time;
    
    @Column(name = "notification_type")
    @Enumerated(EnumType.STRING)
    private NotificationType type;
    
    @Column(name = "is_unread")
    private Boolean unread = true;
    
    @Column(name = "priority_level")
    @Enumerated(EnumType.STRING)
    private PriorityLevel priority = PriorityLevel.MEDIUM;
    
    @Column(name = "expiry_date")
    private LocalDateTime expiryDate;
    
    @Column(name = "target_audience")
    private String targetAudience; // e.g., "ALL", "MEMBERS_ONLY", "SOCIETY_SPECIFIC"
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "related_event_id")
    private UpcomingEvent relatedEvent;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "society_id")
    private Society society;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (time == null) {
            time = LocalDateTime.now();
        }
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    
    public enum NotificationType {
        EVENT, ACHIEVEMENT, REMINDER, MEMBERSHIP, NEWSLETTER, ANNOUNCEMENT, DEADLINE
    }
    
    public enum PriorityLevel {
        LOW, MEDIUM, HIGH, URGENT
    }
}
