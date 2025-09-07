package com.example.demo.repository;

import com.example.demo.model.Achievement;
import com.example.demo.model.Society;
import com.example.demo.model.Council;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AchievementRepository extends JpaRepository<Achievement, Long> {
    
    List<Achievement> findAllByOrderByYearDesc();
    
    Page<Achievement> findAllByOrderByYearDesc(Pageable pageable);
    
    List<Achievement> findByYear(String year);
    
    List<Achievement> findByYearOrderByAchievementDateDesc(String year);
    
    List<Achievement> findBySociety(Society society);
    
    List<Achievement> findByCouncil(Council council);
    
    List<Achievement> findByIsFeaturedTrueOrderByAchievementDateDesc();
    
    List<Achievement> findByAwardCategory(String awardCategory);
    
    @Query("SELECT a FROM Achievement a WHERE a.year BETWEEN :startYear AND :endYear ORDER BY a.year DESC")
    List<Achievement> findAchievementsByYearRange(@Param("startYear") String startYear, 
                                                 @Param("endYear") String endYear);
    
    @Query("SELECT a FROM Achievement a WHERE LOWER(a.title) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "OR LOWER(a.description) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Achievement> searchAchievementsByKeyword(@Param("keyword") String keyword);
    
    @Query("SELECT a FROM Achievement a WHERE a.recipientName = :recipientName ORDER BY a.achievementDate DESC")
    List<Achievement> findAchievementsByRecipient(@Param("recipientName") String recipientName);
    
    @Query("SELECT a FROM Achievement a WHERE a.awardingOrganization = :organization ORDER BY a.achievementDate DESC")
    List<Achievement> findAchievementsByAwardingOrganization(@Param("organization") String organization);
    
    @Query("SELECT a.year, COUNT(a) FROM Achievement a GROUP BY a.year ORDER BY a.year DESC")
    List<Object[]> getAchievementCountByYear();
    
    @Query("SELECT a.awardCategory, COUNT(a) FROM Achievement a WHERE a.awardCategory IS NOT NULL " +
           "GROUP BY a.awardCategory ORDER BY COUNT(a) DESC")
    List<Object[]> getAchievementCountByCategory();
    
    long countByYear(String year);
    
    long countByIsFeaturedTrue();
}
