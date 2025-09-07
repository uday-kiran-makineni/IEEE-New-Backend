package com.example.demo.dto;

import com.example.demo.model.UpcomingEvent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpcomingEventDTO {
    private Long id;
    private String title;
    private LocalDateTime eventDate;
    private String image;
    private String description;
    private String registrations;
    private String hostingBranchName;
    private String hostingBranchLogo;
    private String venue;
    private LocalDateTime registrationDeadline;
    private Integer maxParticipants;
    private Double registrationFee;
    private Boolean isRegistrationOpen;
    private UpcomingEvent.EventType eventType;
    private Long societyId;
    private String societyName;
    private Long councilId;
    private String councilName;
}
