package com.example.demo.repository;

import com.example.demo.model.Society;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SocietyRepository extends JpaRepository<Society, Long> {
    
    List<Society> findByIsActiveTrue();
    
    List<Society> findByIsActiveTrueOrderByNameAsc();
    
    Optional<Society> findByNameIgnoreCase(String name);
    
    @Query("SELECT s FROM Society s WHERE s.isActive = true AND s.memberCount >= :minMembers")
    List<Society> findActiveSocietiesWithMinMembers(@Param("minMembers") Integer minMembers);
    
    @Query("SELECT s FROM Society s WHERE s.isActive = true AND s.establishedYear >= :year")
    List<Society> findActiveSocietiesEstablishedAfter(@Param("year") Integer year);
    
    @Query("SELECT s FROM Society s WHERE s.isActive = true AND LOWER(s.name) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Society> searchSocietiesByKeyword(@Param("keyword") String keyword);
    
    long countByIsActiveTrue();
}
