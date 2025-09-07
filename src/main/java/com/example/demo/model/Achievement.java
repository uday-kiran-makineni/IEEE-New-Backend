package com.example.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "achievements")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Achievement {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 500)
    private String title;
    
    @Column(nullable = false, length = 4)
    private String year;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Column(name = "image_url")
    private String image;
    
    @Column(name = "award_category")
    private String awardCategory;
    
    @Column(name = "awarding_organization")
    private String awardingOrganization;
    
    @Column(name = "recipient_name")
    private String recipientName;
    
    @Column(name = "achievement_date")
    private LocalDateTime achievementDate;
    
    @Column(name = "is_featured")
    private Boolean isFeatured = false;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "society_id")
    private Society society;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "council_id")
    private Council council;
    
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
