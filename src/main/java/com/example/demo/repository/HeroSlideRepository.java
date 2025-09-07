package com.example.demo.repository;

import com.example.demo.model.HeroSlide;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HeroSlideRepository extends JpaRepository<HeroSlide, Long> {
    
    List<HeroSlide> findByIsActiveTrueOrderByDisplayOrderAsc();
    
    List<HeroSlide> findAllByOrderByDisplayOrderAsc();
    
    @Query("SELECT MAX(h.displayOrder) FROM HeroSlide h")
    Integer findMaxDisplayOrder();
    
    long countByIsActiveTrue();
}
