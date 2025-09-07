package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AchievementDTO {
    private Long id;
    private String title;
    private String year;
    private String description;
    private String image;
    private String awardCategory;
    private String awardingOrganization;
    private String recipientName;
    private LocalDateTime achievementDate;
    private Boolean isFeatured;
    private Long societyId;
    private String societyName;
    private Long councilId;
    private String councilName;
}
