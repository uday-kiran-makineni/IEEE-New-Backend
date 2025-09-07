package com.example.demo.repository;

import com.example.demo.model.UpcomingEvent;
import com.example.demo.model.Society;
import com.example.demo.model.Council;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface UpcomingEventRepository extends JpaRepository<UpcomingEvent, Long> {
    
    List<UpcomingEvent> findAllByOrderByEventDateAsc();
    
    Page<UpcomingEvent> findAllByOrderByEventDateAsc(Pageable pageable);
    
    List<UpcomingEvent> findBySociety(Society society);
    
    List<UpcomingEvent> findByCouncil(Council council);
    
    List<UpcomingEvent> findByEventDateAfterOrderByEventDateAsc(LocalDateTime currentDate);
    
    List<UpcomingEvent> findByIsRegistrationOpenTrueOrderByEventDateAsc();
    
    List<UpcomingEvent> findByEventType(UpcomingEvent.EventType eventType);
    
    @Query("SELECT ue FROM UpcomingEvent ue WHERE ue.eventDate BETWEEN :startDate AND :endDate ORDER BY ue.eventDate ASC")
    List<UpcomingEvent> findEventsByDateRange(@Param("startDate") LocalDateTime startDate, 
                                             @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT ue FROM UpcomingEvent ue WHERE ue.registrationDeadline >= :currentDate " +
           "AND ue.isRegistrationOpen = true ORDER BY ue.registrationDeadline ASC")
    List<UpcomingEvent> findEventsWithOpenRegistration(@Param("currentDate") LocalDateTime currentDate);
    
    @Query("SELECT ue FROM UpcomingEvent ue WHERE LOWER(ue.title) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "OR LOWER(ue.description) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<UpcomingEvent> searchEventsByKeyword(@Param("keyword") String keyword);
    
    @Query("SELECT ue FROM UpcomingEvent ue WHERE ue.registrationFee = 0 AND ue.eventDate > :currentDate ORDER BY ue.eventDate ASC")
    List<UpcomingEvent> findFreeUpcomingEvents(@Param("currentDate") LocalDateTime currentDate);
    
    @Query("SELECT ue FROM UpcomingEvent ue WHERE ue.hostingBranchName = :branchName ORDER BY ue.eventDate ASC")
    List<UpcomingEvent> findEventsByHostingBranch(@Param("branchName") String branchName);
    
    @Query("SELECT ue FROM UpcomingEvent ue WHERE ue.registrationDeadline < :currentDate " +
           "AND ue.isRegistrationOpen = true")
    List<UpcomingEvent> findEventsWithExpiredRegistration(@Param("currentDate") LocalDateTime currentDate);
    
    long countByEventDateAfter(LocalDateTime currentDate);
    
    long countByIsRegistrationOpenTrue();
}
