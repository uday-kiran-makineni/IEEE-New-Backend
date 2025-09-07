package com.example.demo.dto;

import com.example.demo.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    private String email;
    private String fullName;
    private String password;
    private String phoneNumber;
    private String studentId;
    private String department;
    private Integer yearOfStudy;
    private String ieeeMembershipId;
    private User.UserRole role = User.UserRole.MEMBER;
    private Long entityId;  // Added entityId field
    private Boolean isActive = true;  // Added isActive field
    private Boolean emailVerified = true;  // Added emailVerified field
}
