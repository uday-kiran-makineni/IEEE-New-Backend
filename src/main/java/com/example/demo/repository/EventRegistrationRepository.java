package com.example.demo.repository;

import com.example.demo.model.EventRegistration;
import com.example.demo.model.User;
import com.example.demo.model.UpcomingEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface EventRegistrationRepository extends JpaRepository<EventRegistration, Long> {
    
    List<EventRegistration> findByUser(User user);
    
    List<EventRegistration> findByEvent(UpcomingEvent event);
    
    List<EventRegistration> findByUserAndEvent(User user, UpcomingEvent event);
    
    Optional<EventRegistration> findByUserIdAndEventId(Long userId, Long eventId);
    
    List<EventRegistration> findByStatus(EventRegistration.RegistrationStatus status);
    
    List<EventRegistration> findByPaymentStatus(EventRegistration.PaymentStatus paymentStatus);
    
    List<EventRegistration> findByAttendanceStatus(EventRegistration.AttendanceStatus attendanceStatus);
    
    List<EventRegistration> findByEventAndStatus(UpcomingEvent event, EventRegistration.RegistrationStatus status);
    
    @Query("SELECT er FROM EventRegistration er WHERE er.event = :event AND er.status = 'CONFIRMED' ORDER BY er.registrationDate ASC")
    List<EventRegistration> findConfirmedRegistrationsByEvent(@Param("event") UpcomingEvent event);
    
    @Query("SELECT er FROM EventRegistration er WHERE er.user = :user ORDER BY er.registrationDate DESC")
    List<EventRegistration> findRegistrationsByUserOrderedByDate(@Param("user") User user);
    
    @Query("SELECT er FROM EventRegistration er WHERE er.registrationDate BETWEEN :startDate AND :endDate")
    List<EventRegistration> findRegistrationsByDateRange(@Param("startDate") LocalDateTime startDate, 
                                                         @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT er FROM EventRegistration er WHERE er.event = :event AND er.paymentStatus = 'PAID'")
    List<EventRegistration> findPaidRegistrationsByEvent(@Param("event") UpcomingEvent event);
    
    @Query("SELECT er FROM EventRegistration er WHERE er.event = :event AND er.attendanceStatus = 'ATTENDED'")
    List<EventRegistration> findAttendedRegistrationsByEvent(@Param("event") UpcomingEvent event);
    
    @Query("SELECT er FROM EventRegistration er WHERE er.certificateIssued = false AND er.attendanceStatus = 'ATTENDED'")
    List<EventRegistration> findRegistrationsEligibleForCertificate();
    
    @Query("SELECT er FROM EventRegistration er WHERE er.feedbackRating >= :minRating ORDER BY er.feedbackRating DESC")
    List<EventRegistration> findRegistrationsByMinFeedbackRating(@Param("minRating") Integer minRating);
    
    @Query("SELECT COUNT(er) FROM EventRegistration er WHERE er.event = :event")
    long countRegistrationsByEvent(@Param("event") UpcomingEvent event);
    
    @Query("SELECT COUNT(er) FROM EventRegistration er WHERE er.event = :event AND er.status = :status")
    long countRegistrationsByEventAndStatus(@Param("event") UpcomingEvent event, 
                                           @Param("status") EventRegistration.RegistrationStatus status);
    
    @Query("SELECT SUM(er.paymentAmount) FROM EventRegistration er WHERE er.event = :event AND er.paymentStatus = 'PAID'")
    Double getTotalRevenueByEvent(@Param("event") UpcomingEvent event);
    
    @Query("SELECT AVG(er.feedbackRating) FROM EventRegistration er WHERE er.event = :event AND er.feedbackRating IS NOT NULL")
    Double getAverageFeedbackRatingByEvent(@Param("event") UpcomingEvent event);
    
    @Query("SELECT er.status, COUNT(er) FROM EventRegistration er WHERE er.event = :event GROUP BY er.status")
    List<Object[]> getRegistrationStatusCountByEvent(@Param("event") UpcomingEvent event);
    
    boolean existsByUserAndEvent(User user, UpcomingEvent event);
    
    long countByStatus(EventRegistration.RegistrationStatus status);
    
    long countByPaymentStatus(EventRegistration.PaymentStatus paymentStatus);
    
    long countByAttendanceStatus(EventRegistration.AttendanceStatus attendanceStatus);
}
