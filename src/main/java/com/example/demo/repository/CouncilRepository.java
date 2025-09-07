package com.example.demo.repository;

import com.example.demo.model.Council;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CouncilRepository extends JpaRepository<Council, Long> {
    
    List<Council> findByIsActiveTrue();
    
    List<Council> findByIsActiveTrueOrderByNameAsc();
    
    Optional<Council> findByNameIgnoreCase(String name);
    
    List<Council> findByChairPerson(String chairPerson);
    
    @Query("SELECT c FROM Council c WHERE c.isActive = true AND c.establishedYear >= :year")
    List<Council> findActiveCouncilsEstablishedAfter(@Param("year") Integer year);
    
    @Query("SELECT c FROM Council c WHERE c.isActive = true AND LOWER(c.name) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Council> searchCouncilsByKeyword(@Param("keyword") String keyword);
    
    @Query("SELECT c FROM Council c WHERE c.isActive = true AND c.websiteUrl IS NOT NULL")
    List<Council> findActiveCouncilsWithWebsite();
    
    long countByIsActiveTrue();
}
