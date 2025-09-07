package com.example.demo.dto;

import com.example.demo.model.Notification;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationDTO {
    private Long id;
    private String title;
    private String message;
    private LocalDateTime time;
    private Notification.NotificationType type;
    private Boolean unread;
    private Notification.PriorityLevel priority;
    private LocalDateTime expiryDate;
    private String targetAudience;
    private Long relatedEventId;
    private String relatedEventTitle;
    private Long societyId;
    private String societyName;
}
