package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HeroSlideDTO {
    private Long id;
    private String title;
    private String subtitle;
    private String image;
    private String description;
    private Integer displayOrder;
    private Boolean isActive;
    private String buttonText;
    private String buttonUrl;
    private String backgroundColor;
    private String textColor;
}
