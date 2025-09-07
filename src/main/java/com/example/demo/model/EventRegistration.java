package com.example.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "event_registrations")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventRegistration {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false)
    private UpcomingEvent event;
    
    @Column(name = "registration_date")
    private LocalDateTime registrationDate;
    
    @Column(name = "registration_status")
    @Enumerated(EnumType.STRING)
    private RegistrationStatus status = RegistrationStatus.PENDING;
    
    @Column(name = "payment_status")
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus = PaymentStatus.PENDING;
    
    @Column(name = "payment_amount")
    private Double paymentAmount;
    
    @Column(name = "payment_reference")
    private String paymentReference;
    
    @Column(name = "special_requirements", columnDefinition = "TEXT")
    private String specialRequirements;
    
    @Column(name = "attendance_status")
    @Enumerated(EnumType.STRING)
    private AttendanceStatus attendanceStatus = AttendanceStatus.REGISTERED;
    
    @Column(name = "feedback_rating")
    private Integer feedbackRating;
    
    @Column(name = "feedback_comments", columnDefinition = "TEXT")
    private String feedbackComments;
    
    @Column(name = "certificate_issued")
    private Boolean certificateIssued = false;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (registrationDate == null) {
            registrationDate = LocalDateTime.now();
        }
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    
    public enum RegistrationStatus {
        PENDING, CONFIRMED, CANCELLED, WAITLISTED
    }
    
    public enum PaymentStatus {
        PENDING, PAID, FAILED, REFUNDED
    }
    
    public enum AttendanceStatus {
        REGISTERED, ATTENDED, ABSENT, PARTIALLY_ATTENDED
    }
}
