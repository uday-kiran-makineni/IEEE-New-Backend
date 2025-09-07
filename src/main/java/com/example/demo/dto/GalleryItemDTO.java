package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GalleryItemDTO {
    private Long id;
    private String img;
    private String url;
    private Integer height;
    private Integer width;
    private String title;
    private String description;
    private String altText;
    private String category;
    private String tags;
    private Boolean isFeatured;
    private LocalDateTime uploadDate;
    private Long pastEventId;
    private String pastEventTitle;
    private Long upcomingEventId;
    private String upcomingEventTitle;
    private Long societyId;
    private String societyName;
    private Long councilId;
    private String councilName;
}
