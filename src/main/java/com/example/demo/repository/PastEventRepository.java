package com.example.demo.repository;

import com.example.demo.model.PastEvent;
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
public interface PastEventRepository extends JpaRepository<PastEvent, Long> {
    
    List<PastEvent> findAllByOrderByEventDateDesc();
    
    Page<PastEvent> findAllByOrderByEventDateDesc(Pageable pageable);
    
    List<PastEvent> findBySociety(Society society);
    
    List<PastEvent> findByCouncil(Council council);
    
    List<PastEvent> findByEventDateBetweenOrderByEventDateDesc(LocalDateTime startDate, LocalDateTime endDate);
    
    @Query("SELECT pe FROM PastEvent pe WHERE pe.eventDate >= :startDate AND pe.eventDate <= :endDate")
    List<PastEvent> findEventsByDateRange(@Param("startDate") LocalDateTime startDate, 
                                         @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT pe FROM PastEvent pe WHERE LOWER(pe.title) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "OR LOWER(pe.description) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<PastEvent> searchEventsByKeyword(@Param("keyword") String keyword);
    
    @Query("SELECT pe FROM PastEvent pe WHERE pe.feedbackRating >= :minRating ORDER BY pe.feedbackRating DESC")
    List<PastEvent> findEventsByMinRating(@Param("minRating") Double minRating);
    
    @Query("SELECT pe FROM PastEvent pe WHERE pe.hostingBranchName = :branchName ORDER BY pe.eventDate DESC")
    List<PastEvent> findEventsByHostingBranch(@Param("branchName") String branchName);
    
    @Query("SELECT EXTRACT(YEAR FROM pe.eventDate) as year, COUNT(pe) as count " +
           "FROM PastEvent pe GROUP BY EXTRACT(YEAR FROM pe.eventDate) ORDER BY year DESC")
    List<Object[]> getEventCountByYear();
    
    long countByEventDateBetween(LocalDateTime startDate, LocalDateTime endDate);
}
