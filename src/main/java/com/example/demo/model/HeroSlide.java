package com.example.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "hero_slides")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HeroSlide {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 500)
    private String title;
    
    @Column(name = "subtitle", length = 500)
    private String subtitle;
    
    @Column(name = "image_url", nullable = false)
    private String image;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Column(name = "display_order")
    private Integer displayOrder;
    
    @Column(name = "is_active")
    private Boolean isActive = true;
    
    @Column(name = "button_text")
    private String buttonText;
    
    @Column(name = "button_url")
    private String buttonUrl;
    
    @Column(name = "background_color")
    private String backgroundColor;
    
    @Column(name = "text_color")
    private String textColor;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
