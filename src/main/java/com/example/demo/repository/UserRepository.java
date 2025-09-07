package com.example.demo.repository;

import com.example.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    Optional<User> findByEmail(String email);
    
    Optional<User> findByEmailAndIsActiveTrue(String email);
    
    Optional<User> findByStudentId(String studentId);
    
    Optional<User> findByIeeeMembershipId(String ieeeMembershipId);
    
    Optional<User> findByVerificationToken(String verificationToken);
    
    Optional<User> findByResetPasswordToken(String resetPasswordToken);
    
    List<User> findByRole(User.UserRole role);
    
    List<User> findByIsActiveTrueOrderByFullNameAsc();
    
    List<User> findByDepartment(String department);
    
    List<User> findByYearOfStudy(Integer yearOfStudy);
    
    List<User> findByEmailVerifiedTrue();
    
    List<User> findByEmailVerifiedFalse();
    
    @Query("SELECT u FROM User u WHERE u.isActive = true AND u.role = :role ORDER BY u.fullName ASC")
    List<User> findActiveUsersByRole(@Param("role") User.UserRole role);
    
    @Query("SELECT u FROM User u WHERE u.isActive = true AND u.department = :department AND u.yearOfStudy = :year")
    List<User> findUsersByDepartmentAndYear(@Param("department") String department, 
                                           @Param("year") Integer year);
    
    @Query("SELECT u FROM User u WHERE LOWER(u.fullName) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "OR LOWER(u.email) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "OR LOWER(u.studentId) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<User> searchUsersByKeyword(@Param("keyword") String keyword);
    
    @Query("SELECT u FROM User u WHERE u.lastLogin BETWEEN :startDate AND :endDate ORDER BY u.lastLogin DESC")
    List<User> findUsersByLastLoginRange(@Param("startDate") LocalDateTime startDate, 
                                        @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT u.department, COUNT(u) FROM User u WHERE u.isActive = true AND u.department IS NOT NULL " +
           "GROUP BY u.department ORDER BY COUNT(u) DESC")
    List<Object[]> getUserCountByDepartment();
    
    @Query("SELECT u.yearOfStudy, COUNT(u) FROM User u WHERE u.isActive = true AND u.yearOfStudy IS NOT NULL " +
           "GROUP BY u.yearOfStudy ORDER BY u.yearOfStudy ASC")
    List<Object[]> getUserCountByYearOfStudy();
    
    @Query("SELECT u.role, COUNT(u) FROM User u WHERE u.isActive = true GROUP BY u.role")
    List<Object[]> getUserCountByRole();
    
    long countByIsActiveTrue();
    
    long countByRole(User.UserRole role);
    
    long countByEmailVerifiedTrue();
    
    long countByDepartment(String department);
    
    boolean existsByEmail(String email);
    
    boolean existsByStudentId(String studentId);
    
    boolean existsByIeeeMembershipId(String ieeeMembershipId);
}
