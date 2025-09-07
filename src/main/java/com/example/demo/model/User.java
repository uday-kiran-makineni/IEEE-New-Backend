package com.example.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String email;
    
    @Column(name = "full_name", nullable = false)
    private String fullName;
    
    @Column(nullable = false)
    private String password;
    
    @Column(name = "phone_number")
    private String phoneNumber;
    
    @Column(name = "student_id")
    private String studentId;
    
    @Column(name = "department")
    private String department;
    
    @Column(name = "year_of_study")
    private Integer yearOfStudy;
    
    @Column(name = "ieee_membership_id")
    private String ieeeMembershipId;
    
    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private UserRole role = UserRole.MEMBER;
    
    @Column(name = "is_active")
    private Boolean isActive = true;
    
    @Column(name = "profile_image_url")
    private String profileImageUrl;
    
    @Column(name = "bio", columnDefinition = "TEXT")
    private String bio;
    
    @Column(name = "linkedin_url")
    private String linkedinUrl;
    
    @Column(name = "github_url")
    private String githubUrl;
    
    @Column(name = "interests")
    private String interests; // Comma-separated interests
    
    @Column(name = "email_verified")
    private Boolean emailVerified = false;
    
    @Column(name = "verification_token")
    private String verificationToken;
    
    @Column(name = "reset_password_token")
    private String resetPasswordToken;
    
    @Column(name = "reset_password_expires")
    private LocalDateTime resetPasswordExpires;
    
    @Column(name = "last_login")
    private LocalDateTime lastLogin;
    
    @Column(name = "entity_id")
    private Long entityId; // Society ID or Council ID for role-based access
    
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
    
    public enum UserRole {
        ADMIN, EXECUTIVE, MODERATOR, MEMBER, GUEST, SOCIETY_ADMIN, COUNCIL_ADMIN
    }
}
