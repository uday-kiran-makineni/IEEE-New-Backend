package com.example.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "past_events")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PastEvent {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 500)
    private String title;
    
    @Column(name = "event_date")
    private LocalDateTime eventDate;
    
    @Column(name = "image_url")
    private String image;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Column(name = "participants_count")
    private String participants;
    
    @Column(name = "hosting_branch_name")
    private String hostingBranchName;
    
    @Column(name = "hosting_branch_logo")
    private String hostingBranchLogo;
    
    @Column(name = "venue")
    private String venue;
    
    @Column(name = "duration_hours")
    private Integer durationHours;
    
    @Column(name = "feedback_rating")
    private Double feedbackRating;
    
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
