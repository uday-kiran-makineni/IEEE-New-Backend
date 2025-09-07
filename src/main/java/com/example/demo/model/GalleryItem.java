package com.example.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "gallery_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GalleryItem {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "image_url", nullable = false)
    private String img;
    
    @Column(name = "link_url")
    private String url;
    
    @Column(name = "image_height")
    private Integer height;
    
    @Column(name = "image_width")
    private Integer width;
    
    @Column(name = "title")
    private String title;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Column(name = "alt_text")
    private String altText;
    
    @Column(name = "category")
    private String category;
    
    @Column(name = "tags")
    private String tags; // Comma-separated tags
    
    @Column(name = "is_featured")
    private Boolean isFeatured = false;
    
    @Column(name = "upload_date")
    private LocalDateTime uploadDate;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "past_event_id")
    private PastEvent pastEvent;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "upcoming_event_id")
    private UpcomingEvent upcomingEvent;
    
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
        if (uploadDate == null) {
            uploadDate = LocalDateTime.now();
        }
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
