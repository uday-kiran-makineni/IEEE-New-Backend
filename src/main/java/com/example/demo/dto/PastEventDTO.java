package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PastEventDTO {
    private Long id;
    private String title;
    private LocalDateTime eventDate;
    private String image;
    private String description;
    private String participants;
    private String hostingBranchName;
    private String hostingBranchLogo;
    private String venue;
    private Integer durationHours;
    private Double feedbackRating;
    private Long societyId;
    private String societyName;
    private Long councilId;
    private String councilName;
}
