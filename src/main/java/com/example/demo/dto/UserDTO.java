package com.example.demo.dto;

import com.example.demo.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Long id;
    private String email;
    private String fullName;
    private String phoneNumber;
    private String studentId;
    private String department;
    private Integer yearOfStudy;
    private String ieeeMembershipId;
    private User.UserRole role;
    private Boolean isActive;
    private String profileImageUrl;
    private String bio;
    private String linkedinUrl;
    private String githubUrl;
    private String interests;
    private Boolean emailVerified;
    private LocalDateTime lastLogin;
    private Long entityId; // Society ID or Council ID for role-based access
}
