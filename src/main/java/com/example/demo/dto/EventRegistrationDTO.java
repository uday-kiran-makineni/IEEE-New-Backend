package com.example.demo.dto;

import com.example.demo.model.EventRegistration;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventRegistrationDTO {
    private Long id;
    private Long userId;
    private String userFullName;
    private String userEmail;
    private Long eventId;
    private String eventTitle;
    private LocalDateTime registrationDate;
    private EventRegistration.RegistrationStatus status;
    private EventRegistration.PaymentStatus paymentStatus;
    private Double paymentAmount;
    private String paymentReference;
    private String specialRequirements;
    private EventRegistration.AttendanceStatus attendanceStatus;
    private Integer feedbackRating;
    private String feedbackComments;
    private Boolean certificateIssued;
}
