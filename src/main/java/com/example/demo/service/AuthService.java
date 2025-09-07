package com.example.demo.service;

import com.example.demo.dto.*;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AuthService {

    private final UserRepository userRepository;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public LoginResponse login(LoginRequest loginRequest) {
        log.info("Attempting login for email: {}", loginRequest.getEmail());
        
        try {
            // Verify credentials
            boolean isValidPassword = userService.verifyPassword(loginRequest.getEmail(), loginRequest.getPassword());
            
            if (!isValidPassword) {
                throw new RuntimeException("Invalid email or password");
            }

            // Get user details
            Optional<UserDTO> userOpt = userService.getUserByEmail(loginRequest.getEmail());
            if (userOpt.isEmpty()) {
                throw new RuntimeException("User not found");
            }

            UserDTO user = userOpt.get();
            
            // Generate token (simplified - in production use JWT)
            String token = generateToken(user);
            
            log.info("Login successful for user: {}", user.getEmail());
            return new LoginResponse(token, user);
            
        } catch (Exception e) {
            log.error("Login failed for email: {}", loginRequest.getEmail(), e);
            throw new RuntimeException("Login failed: " + e.getMessage());
        }
    }

    public UserDTO register(RegisterRequest registerRequest) {
        log.info("Attempting to register user with email: {}", registerRequest.getEmail());
        log.info("Register request details: entityId={}, role={}, isActive={}, emailVerified={}", 
            registerRequest.getEntityId(), registerRequest.getRole(), 
            registerRequest.getIsActive(), registerRequest.getEmailVerified());
        
        try {
            // Check if user already exists
            if (userRepository.existsByEmail(registerRequest.getEmail())) {
                throw new RuntimeException("User with this email already exists");
            }
            
            if (registerRequest.getStudentId() != null && userRepository.existsByStudentId(registerRequest.getStudentId())) {
                throw new RuntimeException("User with this student ID already exists");
            }

            // Create new user
            User user = new User();
            user.setEmail(registerRequest.getEmail());
            user.setFullName(registerRequest.getFullName());
            user.setPassword(registerRequest.getPassword()); // Will be encoded in UserService
            user.setPhoneNumber(registerRequest.getPhoneNumber());
            user.setStudentId(registerRequest.getStudentId());
            user.setDepartment(registerRequest.getDepartment());
            user.setYearOfStudy(registerRequest.getYearOfStudy());
            user.setIeeeMembershipId(registerRequest.getIeeeMembershipId());
            user.setRole(registerRequest.getRole() != null ? registerRequest.getRole() : User.UserRole.MEMBER);
            
            // Set entityId, isActive, and emailVerified from registerRequest
            user.setEntityId(registerRequest.getEntityId());
            user.setIsActive(registerRequest.getIsActive() != null ? registerRequest.getIsActive() : true);
            user.setEmailVerified(registerRequest.getEmailVerified() != null ? registerRequest.getEmailVerified() : false);

            log.info("Creating user with entityId: {}", user.getEntityId());
            
            UserDTO createdUser = userService.createUser(user);
            log.info("Registration successful for user: {} with entityId: {}", 
                createdUser.getEmail(), createdUser.getEntityId());
            
            return createdUser;
            
        } catch (Exception e) {
            log.error("Registration failed for email: {}", registerRequest.getEmail(), e);
            throw new RuntimeException("Registration failed: " + e.getMessage());
        }
    }

    public boolean validateToken(String token) {
        // Simplified token validation - in production use JWT validation
        return token != null && token.startsWith("ieee_token_") && token.length() > 20;
    }

    public Optional<UserDTO> getUserFromToken(String token) {
        // Simplified token parsing - in production parse JWT
        if (!validateToken(token)) {
            return Optional.empty();
        }
        
        // Extract email from token (simplified)
        String[] parts = token.split("_");
        if (parts.length < 4) {
            return Optional.empty();
        }
        
        String email = parts[3]; // Assuming format: ieee_token_timestamp_email
        return userService.getUserByEmail(email);
    }

    private String generateToken(UserDTO user) {
        // Simplified token generation - in production use JWT
        return "ieee_token_" + System.currentTimeMillis() + "_" + user.getEmail() + "_" + UUID.randomUUID().toString().substring(0, 8);
    }

    public boolean logout(String token) {
        log.info("User logout for token: {}", token.substring(0, Math.min(token.length(), 20)) + "...");
        // In production, add token to blacklist or implement token invalidation
        return true;
    }

    public boolean changePassword(String email, String oldPassword, String newPassword) {
        log.info("Attempting password change for email: {}", email);
        
        try {
            // Verify old password
            if (!userService.verifyPassword(email, oldPassword)) {
                throw new RuntimeException("Current password is incorrect");
            }

            // Update password
            Optional<User> userOpt = userRepository.findByEmailAndIsActiveTrue(email);
            if (userOpt.isEmpty()) {
                throw new RuntimeException("User not found");
            }

            User user = userOpt.get();
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);

            log.info("Password changed successfully for user: {}", email);
            return true;
            
        } catch (Exception e) {
            log.error("Password change failed for email: {}", email, e);
            throw new RuntimeException("Password change failed: " + e.getMessage());
        }
    }
}
