package com.example.demo.service;

import com.example.demo.dto.UserDTO;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public List<UserDTO> getAllUsers() {
        log.info("Fetching all users");
        return userRepository.findByIsActiveTrueOrderByFullNameAsc()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Optional<UserDTO> getUserById(Long id) {
        log.info("Fetching user with id: {}", id);
        return userRepository.findById(id)
                .map(this::convertToDTO);
    }

    public Optional<UserDTO> getUserByEmail(String email) {
        log.info("Fetching user with email: {}", email);
        return userRepository.findByEmailAndIsActiveTrue(email)
                .map(this::convertToDTO);
    }

    public List<UserDTO> getUsersByRole(User.UserRole role) {
        log.info("Fetching users by role: {}", role);
        return userRepository.findActiveUsersByRole(role)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<UserDTO> getUsersByDepartment(String department) {
        log.info("Fetching users by department: {}", department);
        return userRepository.findByDepartment(department)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public UserDTO createUser(User user) {
        log.info("Creating new user: {}", user.getEmail());
        
        // Check if user already exists
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("User with email " + user.getEmail() + " already exists");
        }
        
        // Encode password
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setEmailVerified(false);
        user.setIsActive(true);
        
        User savedUser = userRepository.save(user);
        return convertToDTO(savedUser);
    }

    public Optional<UserDTO> updateUser(Long id, UserDTO userDTO) {
        log.info("Updating user with id: {}", id);
        return userRepository.findById(id)
                .map(existingUser -> {
                    updateUserFields(existingUser, userDTO);
                    User updatedUser = userRepository.save(existingUser);
                    return convertToDTO(updatedUser);
                });
    }

    public boolean deleteUser(Long id) {
        log.info("Deactivating user with id: {}", id);
        return userRepository.findById(id)
                .map(user -> {
                    user.setIsActive(false);
                    userRepository.save(user);
                    return true;
                })
                .orElse(false);
    }

    public boolean verifyPassword(String email, String rawPassword) {
        Optional<User> userOpt = userRepository.findByEmailAndIsActiveTrue(email);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            boolean matches = passwordEncoder.matches(rawPassword, user.getPassword());
            if (matches) {
                // Update last login
                user.setLastLogin(LocalDateTime.now());
                userRepository.save(user);
            }
            return matches;
        }
        return false;
    }

    public List<UserDTO> searchUsers(String keyword) {
        log.info("Searching users with keyword: {}", keyword);
        return userRepository.searchUsersByKeyword(keyword)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<Object[]> getUserCountByDepartment() {
        return userRepository.getUserCountByDepartment();
    }

    public List<Object[]> getUserCountByRole() {
        return userRepository.getUserCountByRole();
    }

    public long getActiveUsersCount() {
        return userRepository.countByIsActiveTrue();
    }

    private UserDTO convertToDTO(User user) {
        return new UserDTO(
                user.getId(),
                user.getEmail(),
                user.getFullName(),
                user.getPhoneNumber(),
                user.getStudentId(),
                user.getDepartment(),
                user.getYearOfStudy(),
                user.getIeeeMembershipId(),
                user.getRole(),
                user.getIsActive(),
                user.getProfileImageUrl(),
                user.getBio(),
                user.getLinkedinUrl(),
                user.getGithubUrl(),
                user.getInterests(),
                user.getEmailVerified(),
                user.getLastLogin(),
                user.getEntityId()
        );
    }

    private void updateUserFields(User user, UserDTO userDTO) {
        user.setFullName(userDTO.getFullName());
        user.setPhoneNumber(userDTO.getPhoneNumber());
        user.setDepartment(userDTO.getDepartment());
        user.setYearOfStudy(userDTO.getYearOfStudy());
        user.setIeeeMembershipId(userDTO.getIeeeMembershipId());
        user.setRole(userDTO.getRole() != null ? userDTO.getRole() : user.getRole());
        user.setProfileImageUrl(userDTO.getProfileImageUrl());
        user.setBio(userDTO.getBio());
        user.setLinkedinUrl(userDTO.getLinkedinUrl());
        user.setGithubUrl(userDTO.getGithubUrl());
        user.setInterests(userDTO.getInterests());
        user.setEntityId(userDTO.getEntityId());
    }
}
