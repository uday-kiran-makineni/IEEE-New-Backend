package com.example.demo.controller;

import com.example.demo.dto.*;
import com.example.demo.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        log.info("POST /api/auth/login - Login attempt for email: {}", loginRequest.getEmail());
        
        try {
            LoginResponse response = authService.login(loginRequest);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Login failed", e);
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest) {
        log.info("POST /api/auth/register - Registration attempt for email: {}", registerRequest.getEmail());
        
        try {
            UserDTO user = authService.register(registerRequest);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Registration successful");
            response.put("user", user);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("Registration failed", e);
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String token) {
        log.info("POST /api/auth/logout - Logout request");
        
        try {
            String cleanToken = token.replace("Bearer ", "");
            boolean success = authService.logout(cleanToken);
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Logout successful");
            response.put("success", success);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Logout failed", e);
            Map<String, String> error = new HashMap<>();
            error.put("error", "Logout failed");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    @GetMapping("/verify")
    public ResponseEntity<?> verifyToken(@RequestHeader("Authorization") String token) {
        log.info("GET /api/auth/verify - Token verification request");
        
        try {
            String cleanToken = token.replace("Bearer ", "");
            boolean isValid = authService.validateToken(cleanToken);
            
            if (!isValid) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Invalid token");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
            }

            UserDTO user = authService.getUserFromToken(cleanToken).orElse(null);
            if (user == null) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "User not found");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
            }

            Map<String, Object> response = new HashMap<>();
            response.put("valid", true);
            response.put("user", user);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("Token verification failed", e);
            Map<String, String> error = new HashMap<>();
            error.put("error", "Token verification failed");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }
    }

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(
            @RequestHeader("Authorization") String token,
            @RequestBody Map<String, String> passwordData) {
        log.info("POST /api/auth/change-password - Password change request");
        
        try {
            String cleanToken = token.replace("Bearer ", "");
            UserDTO user = authService.getUserFromToken(cleanToken).orElse(null);
            
            if (user == null) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Invalid token");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
            }

            String oldPassword = passwordData.get("oldPassword");
            String newPassword = passwordData.get("newPassword");
            
            boolean success = authService.changePassword(user.getEmail(), oldPassword, newPassword);
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Password changed successfully");
            response.put("success", success);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("Password change failed", e);
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(@RequestHeader("Authorization") String token) {
        log.info("GET /api/auth/profile - Profile request");
        
        try {
            String cleanToken = token.replace("Bearer ", "");
            UserDTO user = authService.getUserFromToken(cleanToken).orElse(null);
            
            if (user == null) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Invalid token");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
            }

            return ResponseEntity.ok(user);
            
        } catch (Exception e) {
            log.error("Profile fetch failed", e);
            Map<String, String> error = new HashMap<>();
            error.put("error", "Profile fetch failed");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }
    }
}
